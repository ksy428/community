package hello.community.service.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.comment.Comment;
import hello.community.domain.member.Member;
import hello.community.dto.comment.CommentEditDto;
import hello.community.dto.comment.CommentWriteDto;
import hello.community.exception.comment.CommentException;
import hello.community.exception.comment.CommentExceptionType;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.global.security.SecurityUtil;
import hello.community.repository.comment.CommentRepository;
import hello.community.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class CommentServiceImplTest {

	@Autowired
	private CommentService commentService;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MockMvc mockMvc;

	/*	@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 댓글등록() {
			// given
			CommentWriteDto writeDto = new CommentWriteDto();
			writeDto.setContent("댓글11");
	
			// when
			Long writeCommentId = commentService.write(1L, Optional.empty(), writeDto);
	
			// then
			Comment comment = commentRepository.findById(writeCommentId)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
	
			assertThat(comment.getContent()).isEqualTo("댓글11");
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 대댓글등록() {
			// given
			CommentWriteDto writeDto = new CommentWriteDto();
			writeDto.setContent("2번게시글 2번댓글에 대댓글");
	
			// when
			Long writeCommentId = commentService.write(2L, Optional.of(2L), writeDto);
	
			// then
			Comment comment = commentRepository.findById(writeCommentId)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
	
			assertThat(comment.getContent()).isEqualTo("2번게시글 2번댓글에 대댓글");
			assertThat(comment.getParent().getId()).isEqualTo(2L);
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 댓글수정() {
			// given
			CommentEditDto editDto = new CommentEditDto();
			editDto.setContent("댓글수정완료");
	
			// when
			Long editCommentId = commentService.edit(5L, editDto);
	
			// then
			Comment comment = commentRepository.findById(editCommentId)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
	
			assertThat(comment.getContent()).isEqualTo("댓글수정완료");
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 댓글수정_권한없음() {
			// given
			CommentEditDto editDto = new CommentEditDto();
			editDto.setContent("댓글수정완료");
	
			// when
			// then
			assertThrows(CommentException.class, () -> commentService.edit(2L, editDto));
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 댓글삭제_대댓글없음() {
			// given
			Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
					.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
			int commentCount = member.getCommentList().size(); // 댓글 삭제 전 크기
			// when
			Long deleteCommentId = commentService.delete(17L);
	
			// then
			assertThrows(CommentException.class, () -> commentRepository.findById(deleteCommentId)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT)));
			assertThat(member.getCommentList().size()).isEqualTo(commentCount - 1);
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 댓글삭제_대댓글있음() {
			// given
			Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
					.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
			int commentCount = member.getCommentList().size(); // 댓글 삭제 전 크기
			// when
			Long deleteCommentId = commentService.delete(1L);
	
			// then
			Comment comment = commentRepository.findById(deleteCommentId)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
			// 대댓글있으면 삭제되지 않고 상태만 변경
			assertThat(comment.isDeleted()).isEqualTo(true);
			assertThat(member.getCommentList().size()).isEqualTo(commentCount);
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 댓글삭제_대댓글있음_대댓글모두삭제상태() {
			// given
			Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
					.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
			int commentCount = member.getCommentList().size(); // 댓글 삭제 전 크기
			Comment comment = commentRepository.findById(1L)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
	
			List<Comment> childList = comment.getChildList();
			// 자식모두 삭제상태로 변경
			for (Comment child : childList) {
				child.editDeleteStatus();
			}
	
			// when
			Long deleteCommentId = commentService.delete(1L);
	
			// then
			assertThrows(CommentException.class, () -> commentRepository.findById(deleteCommentId)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT)));
			assertThat(member.getCommentList().size()).isEqualTo(commentCount - 4);
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 대댓글삭제_부모있음() {
			// given
	
			CommentWriteDto writeDto = new CommentWriteDto();
			writeDto.setContent("댓글");
			Long commentId = commentService.write(1L, Optional.empty(), writeDto);
	
			CommentWriteDto writeDto2 = new CommentWriteDto();
			writeDto2.setContent("대댓글");
			Long reCommentId = commentService.write(1L, Optional.of(commentId), writeDto);
	
			Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
					.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
			int commentCount = member.getCommentList().size();
	
			// when
			Long deleteCommentId = commentService.delete(reCommentId);
	
			// then
			Comment comment = commentRepository.findById(deleteCommentId)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
	
			assertThat(comment.isDeleted()).isEqualTo(true);
			assertThat(member.getCommentList().size()).isEqualTo(commentCount);
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 대댓글삭제_부모_형제_댓글삭제상태() {
			// given
			Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
					.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
			int commentCount = member.getCommentList().size(); // 댓글 삭제 전 크기
	
			Comment parent = commentRepository.findById(101L)
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
	
			// when
			parent.editDeleteStatus();
			List<Comment> childList = parent.getChildList();
			// 자식들 각각 삭제 마지막은 모두 삭제상태이므로 전부 다 삭제 되어야 함
			for (Comment child : childList) {
				commentService.delete(child.getId());
			}
	
			// then
			assertThrows(CommentException.class, () -> commentRepository.findById(parent.getId())
					.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT)));
			assertThat(member.getCommentList().size()).isEqualTo(commentCount - 4);
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		void 댓글_페이징() {
			// given
			try {
				mockMvc.perform(get("/comment/1/1")).andExpect(status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			// when
	
			// then
		}
	
		@Test
		@WithMockUser(username = "test", roles = "MEMBER")
		@Rollback(false)
		void 댓글_ㅋ() {
			// given
	
			try {
				mockMvc.perform(post("/comment/1")).andExpect(status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			// when
	
			// then
		}*/
}
