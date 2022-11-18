package hello.community.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import hello.community.domain.media.Media;
import hello.community.domain.post.Post;
import hello.community.dto.member.MemberInfoDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostInfoDto {

	private Long postId;
	private String title;
	private String content;
	private Long hit;
	private Long recommend;
	private List<Media> uploadMedia;
	private MemberInfoDto memberInfoDto;
	private LocalDateTime createdDate;

	
	public PostInfoDto(Post post) {
		this.postId = post.getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.hit = post.getHit();
		this.recommend = post.getRecommend();
		this.uploadMedia = post.getMediaList();
		this.memberInfoDto = new MemberInfoDto(post.getWriter());
		this.createdDate = post.getCreatedDate();
	}
}
