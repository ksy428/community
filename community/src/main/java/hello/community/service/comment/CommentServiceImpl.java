package hello.community.service.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.comment.Comment;
import hello.community.dto.comment.CommentEditDto;
import hello.community.dto.comment.CommentPagingDto;
import hello.community.dto.comment.CommentWriteDto;
import hello.community.exception.comment.CommentException;
import hello.community.exception.comment.CommentExceptionType;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.exception.post.PostException;
import hello.community.exception.post.PostExceptionType;
import hello.community.global.security.SecurityUtil;
import hello.community.repository.comment.CommentRepository;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentServiceImpl implements CommentService{
	
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	

	@Override
	@Transactional
	public Long write(Long postId, Optional<Long> commentId, CommentWriteDto writeDto) {
		
		Comment comment = writeDto.toEntity();
		
			
		comment.setMember(memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(()-> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
		
		comment.setPost(postRepository.findById(postId)
				.orElseThrow(()-> new PostException(PostExceptionType.NOT_FOUND_POST)));
		
		Long groupId = commentRepository.findMaxGroupId().orElse(0L);
		//대댓글일경우
		if(commentId.isPresent()){
			comment.setParent(commentRepository.findById(commentId.get())
					.orElseThrow(()-> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT)));
			comment.setGroupId(comment.getParent().getGroupId());			
		}
		//댓글일경우
		else {
			comment.setGroupId(groupId + 1);
		}
					
		commentRepository.save(comment);
		
		return comment.getId();
	}


	@Override
	@Transactional
	public Long edit(Long commentId, CommentEditDto editDto) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(()-> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
		
		if(!comment.getWriter().getLoginId().equals(SecurityUtil.getLoginMemberId())) {
			throw new CommentException(CommentExceptionType.NOT_AUTHORIZATION_COMMENT);
		}
		
		comment.editContent(editDto.getContent());
		
		return comment.getId();
	}

	@Override
	@Transactional
	public Long delete(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(()-> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
		
		if(!comment.getWriter().getLoginId().equals(SecurityUtil.getLoginMemberId())) {
			throw new CommentException(CommentExceptionType.NOT_AUTHORIZATION_COMMENT);
		}
	 
		//먼저 삭제상태로 바꾼후 DB에서 삭제할 것이 있는지 찾는다
		comment.editDeleteStatus();
		List<Comment> deleteList = findDeleteList(comment);
		//삭제할것이 있으면 DB에서 제거
		if (deleteList.size() != 0) {
			commentRepository.deleteAll(deleteList);
			
			for (Comment deleteComment : deleteList) {
				deleteComment.getWriter().getCommentList().remove(deleteComment);
			}
		}
		return comment.getId();
	}
	
	
	
	//삭제할 comment 찾기
	private List<Comment> findDeleteList(Comment comment){
		List<Comment> result = new ArrayList<>();
			
		Optional.ofNullable(comment.getParent()).ifPresentOrElse(
				//대댓글일경우
				parent -> { 
					//부모가 삭제되어있고 부모의 자식이 모두 삭제되어있으면 전체 삭제
					if(parent.isDeleted() && isAllChildDeleted(parent)) {
						result.add(parent);
						result.addAll(parent.getChildList());
					}
				},
				//댓글일경우
				()-> {
					// 자식이 모두 삭제되어있으면 전체 삭제
					if(isAllChildDeleted(comment)) {
						result.add(comment);
						result.addAll(comment.getChildList());
					}			
				});
		
		return result;
	}
	
	private boolean isAllChildDeleted(Comment comment) {
        return comment.getChildList().stream()
                .map(Comment::isDeleted)
                .filter(isDeleted -> !isDeleted)// 삭제상태인 자식들(true)을 버림
                .findAny()// 남아있는 자식(false)이 있으면 삭제불가능
                .orElse(true);// 남아있는 자식이 없으면 삭제가능 
    }


	@Override
	public CommentPagingDto searchCommentList(Pageable pageable, Long postId, int page) {
		pageable = PageRequest.of( page > 0 ? (page - 1) : 0 ,  150);		
		return new CommentPagingDto(commentRepository.search(pageable, postId));
	}
}
