package hello.community.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import hello.community.domain.media.Media;
import hello.community.domain.post.Post;
import hello.community.global.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostBriefInfo {

	private Long postId;
	private String title;
	private String content;
	private String writerNickname;
	private Long hit;
	private Long recommend;
	private String createdDate;
	private int commentCount;
	private boolean isBest;
	private String thumbnailPath;
	private String boardType;
	private String boardName;
	
	public PostBriefInfo(Post post) {
		this.postId = post.getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.writerNickname = post.getWriter().getNickname();
		this.hit = post.getHit();
		this.recommend = post.getRecommend();
		this.createdDate =  DateUtil.calculateDate(post.getCreatedDate());
		this.commentCount = post.getCommentList().size();
		this.isBest = post.isBest();
		this.thumbnailPath = getThumbnailPath(post);
		this.boardType = post.getBoard().getBoardType();
		this.boardName = post.getBoard().getBoardName();
	}
	
	public String getThumbnailPath(Post post) {
		
		String thumbnailFileName = null;
		
		List<Media> mediaList = post.getMediaList();
			
		//내용중 첫번째 이미지가 썸네일
		for(Media media : mediaList) {
			if(thumbnailFileName == null) {
				thumbnailFileName = media.getStoreName();
			}
			if(post.getContent().indexOf(thumbnailFileName) > post.getContent().indexOf(media.getStoreName())) {
				thumbnailFileName = media.getStoreName();
			}
		}
		
		if(thumbnailFileName == null) {
			return null;
		}
		return "/file/t_" + thumbnailFileName;
	}
}
