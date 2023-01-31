package hello.community.service.comment;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import hello.community.dto.comment.CommentEditDto;
import hello.community.dto.comment.CommentPagingDto;
import hello.community.dto.comment.CommentWriteDto;

public interface CommentService {

	Long write(Long postId, Optional<Long> commentId, CommentWriteDto writeDto);
	
	Long edit(Long commentId, CommentEditDto editDto);
	
	Long delete(Long commentId);
	
	CommentPagingDto searchCommentList(Pageable pageable, Long postId, int page);
}
