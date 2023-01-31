package hello.community.repository.comment;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import hello.community.domain.comment.Comment;

public interface CustomCommentRepository {

	Page<Comment> search(Pageable pageable, Long postId);

}
