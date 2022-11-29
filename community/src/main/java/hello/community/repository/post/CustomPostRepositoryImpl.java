package hello.community.repository.post;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import hello.community.domain.post.Post;

import static hello.community.domain.post.QPost.post;
import static hello.community.domain.member.QMember.member;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository{

	private final JPAQueryFactory query;
	
	
	public CustomPostRepositoryImpl(EntityManager em) {
		
		this.query = new JPAQueryFactory(em);
	}

	@Override
	public Page<Post> search(Pageable pageable, PostSearch postSearch) {
	
			List<Post> results = query
					.selectFrom(post)
					.where(
							typeContain(postSearch)
						   )
					.leftJoin(post.writer, member)
					.fetchJoin()
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.orderBy(post.id.desc())
					.fetch();
			
			JPAQuery<Post> countQuery = query
					.selectFrom(post)
					.where(
							typeContain(postSearch)							
							);
			
		return PageableExecutionUtils.getPage(results, pageable, ()-> countQuery.fetch().size());
	}
	
	private BooleanBuilder typeContain(PostSearch postSearch) {
		
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		
		if(postSearch.getTarget() == null) {
			return booleanBuilder;
		}	
		if(postSearch.getTarget().contains("title")) {
			booleanBuilder.or(post.title.contains(postSearch.getKeyword()));
		}
		if(postSearch.getTarget().contains("content")) {
			booleanBuilder.or(post.content.contains(postSearch.getKeyword()));
		}
		if(postSearch.getTarget().contains("writer")) {
			booleanBuilder.or(post.writer.nickname.contains(postSearch.getKeyword()));
		}
		if(postSearch.getTarget().contains("all")) {
			booleanBuilder.or(post.title.contains(postSearch.getKeyword()));
			booleanBuilder.or(post.content.contains(postSearch.getKeyword()));
			booleanBuilder.or(post.writer.nickname.contains(postSearch.getKeyword()));
		}
		
		return booleanBuilder;
	}
}
