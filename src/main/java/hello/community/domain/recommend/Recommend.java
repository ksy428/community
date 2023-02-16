package hello.community.domain.recommend;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@NoArgsConstructor()
public class Recommend {

	@Id
	@Column(name = "recommend_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member writer;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;



	// 연관관계메서드
	public void setWriter(Member writer) {
		this.writer = writer;
		writer.getRecommendListList().add(this);
	}

	public void setPost(Post post) {
		this.post = post;
		post.getRecommendList().add(this);
	}
}
