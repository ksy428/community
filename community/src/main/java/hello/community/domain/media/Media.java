package hello.community.domain.media;

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
public class Media {

	@Id
	@Column(name = "media_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String originMediaName;
	private String storeMediaName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Builder
	public Media(String originMediaName, String storeMediaName) {
		this.originMediaName = originMediaName;
		this.storeMediaName = storeMediaName;
	}

	// 연관관계메서드
	public void setPost(Post post) {
		this.post = post;
		post.getMediaList().add(this);
	}
}
