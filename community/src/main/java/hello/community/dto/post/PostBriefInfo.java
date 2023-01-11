package hello.community.dto.post;

import java.time.LocalDateTime;

import hello.community.domain.post.Post;
import hello.community.global.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostBriefInfo {

	private Long postId;
	private String boardType;
	private String title;
	private String content;
	private String writerNickname;
	private Long hit;
	private Long recommend;
	private String createdDate;
	private int commentCount;
	
	public PostBriefInfo(Post post) {
		this.postId = post.getId();
		this.boardType = post.getBoard().getBoardType();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.writerNickname = post.getWriter().getNickname();
		this.hit = post.getHit();
		this.recommend = post.getRecommend();
		this.createdDate =  DateUtil.calculateDate(post.getCreatedDate());
		this.commentCount = post.getCommentList().size();
	}
}
