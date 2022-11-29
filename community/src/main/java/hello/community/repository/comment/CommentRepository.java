package hello.community.repository.comment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hello.community.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository{

	Optional<Comment> findById(Long id);
	
	@Query("select max(c.groupId) from Comment c")
	Optional<Long> findMaxGroupId();
	
}
