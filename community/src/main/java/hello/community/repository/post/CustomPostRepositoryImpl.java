package hello.community.repository.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import hello.community.domain.post.Post;

import static hello.community.domain.post.QPost.post;
import static hello.community.domain.member.QMember.member;
import static hello.community.domain.board.QBoard.board;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository{

	private final JPAQueryFactory query;
	
	
	public CustomPostRepositoryImpl(EntityManager em) {
		
		this.query = new JPAQueryFactory(em);
	}

	@Override
	public Page<Post> search(Pageable pageable, String boardType, PostSearch postSearch) {
	
			List<Post> results = query
					.selectFrom(post)
					.where(							
							eqBoardType(boardType)
							,eqBest(postSearch.getMode())
							,typeContain(postSearch)
						   )
					.leftJoin(post.writer, member).fetchJoin()
					.leftJoin(post.board, board).fetchJoin()
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
					.fetch();
			
			JPAQuery<Post> countQuery = query
					.selectFrom(post)
					.where(
							eqBoardType(boardType)
							,eqBest(postSearch.getMode())
							,typeContain(postSearch)							
							);

		return PageableExecutionUtils.getPage(results, pageable, ()-> countQuery.fetch().size());
	}
	
	private BooleanExpression eqBoardType(String boardType) {
		
		if(boardType.equals("main") || boardType.equals("best")) {
			return null;
		}
		else {
			return post.board.boardType.eq(boardType);
		}
		
		//return StringUtils.hasText(boardType) ? post.board.boardType.eq(boardType) : null;
	}
	
	private BooleanExpression eqBest(String mode) {
		
		if(StringUtils.hasText(mode)) {
			return mode.equals("best") ? post.isBest.eq(true) : null;
		}		
		return null;
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
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
	    List<OrderSpecifier> orders = new ArrayList<>();
	    // Sort
	    sort.stream().forEach(order -> {
	        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
	        String prop = order.getProperty();
			switch (prop){
	    		case "best":
	    			orders.add(new OrderSpecifier(direction, post.bestDate));   
	    			break;
	    		case "id":
	    			orders.add(new OrderSpecifier(direction, post.id));
	    			break;
			}
	    });
	    return orders;
	}
}
	

