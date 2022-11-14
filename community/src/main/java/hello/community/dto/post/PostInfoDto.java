package hello.community.dto.post;

import java.util.List;

import hello.community.domain.image.Image;
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
	private List<Image> updoaldImages;
	private MemberInfoDto memberInfoDto;

	
	public PostInfoDto(Post post) {
		this.postId = post.getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.hit = post.getHit();
		this.updoaldImages = post.getImageList();
		this.memberInfoDto = new MemberInfoDto(post.getWriter());
	}
}
