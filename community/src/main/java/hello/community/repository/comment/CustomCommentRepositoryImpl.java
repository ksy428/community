package hello.community.repository.comment;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import hello.community.domain.comment.Comment;
import static hello.community.domain.post.QPost.post;
import static hello.community.domain.member.QMember.member;
import static hello.community.domain.comment.QComment.comment;

@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

	private final JPAQueryFactory query;
	
	
	public CustomCommentRepositoryImpl(EntityManager em) {
		
		this.query = new JPAQueryFactory(em);
	}
	
	
	@Override
	public Page<Comment> search(Pageable pageable, Long postId) {
	
		List<Comment> results = query
				.selectFrom(comment)
				.where(
						comment.post.id.eq(postId)			
						)
				.leftJoin(comment.writer, member).fetchJoin()
				.leftJoin(comment.post, post).fetchJoin()
				.leftJoin(comment.parent).fetchJoin()
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(comment.groupId.asc()
						,comment.createdDate.asc())
				.fetch();
		
		JPAQuery<Comment> countQuery = query
				.selectFrom(comment)
				.where(
						comment.post.id.eq(postId)
						);
		
		return PageableExecutionUtils.getPage(results, pageable, ()-> countQuery.fetch().size());
	}
}
