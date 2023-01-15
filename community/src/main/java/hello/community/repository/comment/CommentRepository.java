package hello.community.repository.comment;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hello.community.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository{
	/**
	 * 댓글 수정과 삭제할때만 writer,parnet를 접근하므로 패치조인 안해도될듯
	 */
	//@EntityGraph(attributePaths = {"writer"})
	Optional<Comment> findById(Long id);
	
	@Query("select max(c.groupId) from Comment c")
	Optional<Long> findMaxGroupId();
	
}
