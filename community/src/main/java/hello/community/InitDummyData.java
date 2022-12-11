package hello.community;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import hello.community.domain.comment.Comment;
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
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

@Component
@RequiredArgsConstructor
public class InitDummyData {
	
	private final Init init;
	
	@PostConstruct
	public void init() {
		init.save();
	}
	
	@Component
	@RequiredArgsConstructor
	private static class Init{
		
		private final MemberRepository memberRepository;
		private final PostRepository postRepository;
		private final PasswordEncoder passwordEncoder;
		private final CommentRepository commentRepository;
		
		@Transactional
	    public void save() {
			 //initMember("tpdud0428", "1234", "세영짱짱", "tpdud0428@naver.com");
			 initMember("test", "1234", "짱짱", "test@naver.com");
			 initMember("dummy1", "1234", "1번유저", "dummy1@naver.com");
			 initMember("dummy2", "1234", "2번유저", "dummy2@naver.com");
			 initMember("dummy3", "1234", "3번유저", "dummy3@naver.com");
		
	
			 for(int i=0; i<100;i++) {			 
				 initPost((i+1) +"번 글", (i+1)+"번 내용", 1L);
			 }
			 
			 for(int i=0; i<100; i++) {
				 long groupId = (long) i+1;				 
				 initComment("1번 글_"+ groupId+"번댓글", 1L, 1L, groupId);
			 }
			 
			 for(int i=0; i<10; i++) {
				 long commentNum2 = (long)((i % 3 ) + 1);	 
				 initReComment(commentNum2+"번 댓글_대댓글", 1L, 1L, commentNum2);
			 }
			 
				/*
				 * Long commentId = initComment("댓글", 1L, 150L, 101L); initReComment("대댓글", 1L,
				 * 150L, commentId); initReComment("대댓글", 1L, 150L, commentId);
				 * initReComment("대댓글", 1L, 150L, commentId);
				 */
	    }

		@Transactional
		public void initMember(String loginId, String password, String nickname, String email) {
			 memberRepository.save(Member.builder()			
						.loginId(loginId)
						.password(passwordEncoder.encode(password))
						.nickname(nickname)
						.email(email)
						.build());
		}
		
		@Transactional
		public void initPost(String title, String content, Long memberId) {
			Post post = Post.builder().title(title).content(content).build();
			post.setWriter(memberRepository.findById(memberId).
					orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
			
			postRepository.save(post);
		}	
		
		@Transactional
		public Long initComment(String content, Long memberId, Long postId, Long groupId) {
			Comment comment = Comment.builder().content(content).build();
			
			comment.setMember(memberRepository.findById(memberId)
					.orElseThrow(()-> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
			comment.setPost(postRepository.findById(postId)
					.orElseThrow(()-> new PostException(PostExceptionType.NOT_FOUND_POST)));
			comment.setGroupId(groupId);
	
			commentRepository.save(comment);
			
			return comment.getId();
		}
		
		@Transactional
		public void initReComment(String content, Long memberId, Long postId, Long commentId) {
			Comment comment = Comment.builder().content(content).build();
			
			comment.setMember(memberRepository.findById(memberId)
					.orElseThrow(()-> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
			comment.setPost(postRepository.findById(postId)
					.orElseThrow(()-> new PostException(PostExceptionType.NOT_FOUND_POST)));
			comment.setParent(commentRepository.findById(commentId)
					.orElseThrow(()-> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT)));
			comment.setGroupId(commentId);
			comment.editDeleteStatus();
			
			commentRepository.save(comment);
		}
	}
}



