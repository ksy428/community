package hello.community.domain.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import hello.community.domain.BaseTimeEntity;
import hello.community.domain.comment.Comment;
import hello.community.domain.image.Image;
import hello.community.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	@Lob
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member writer;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Image> imageList = new ArrayList<>();
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Comment> commentList = new ArrayList<>();
	
	private Long hit;
	
	@Builder
	public Post(String title, String content, Member writer) {
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.hit = 0L;
	}
	
	public void editTitle(String title) {
		this.title = title;
	}
	
	public void editContent(String content) {
		this.content = content;
	}
	
	public void initImageList() {
		this.imageList.clear();
	}
	
	public void addHit() {
		this.hit += 1;
	}
	
	//연관관계메서드
	//멤버-게시글
	public void setWriter(Member writer) {
		this.writer = writer;
		writer.getPostList().add(this);
	}	
}
