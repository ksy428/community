package hello.community.repository.comment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.community.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	Optional<Comment> findById(Long id);
}
