package hello.community.domain.image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

	@Id
	@Column(name = "image_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String originImageName;
	private String storeImageName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Builder
	public Image(String originImageName, String storeImageName) {
		this.originImageName = originImageName;
		this.storeImageName = storeImageName;
	}

	// 연관관계메서드
	public void setPost(Post post) {
		this.post = post;
		post.getImageList().add(this);
	}
}
