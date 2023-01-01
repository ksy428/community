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
import hello.community.domain.board.Board;
import hello.community.domain.comment.Comment;
import hello.community.domain.media.Media;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;
	
	private String title;
	
	@Lob
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member writer;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Media> mediaList = new ArrayList<>();
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> commentList = new ArrayList<>();
	
	private Long hit;
	
	private Long recommend;
	
	@Builder
	public Post(String title, String content, Member writer) {
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.hit = 0L;
		this.recommend = 0L;
	}
	
	public void editTitle(String title) {
		this.title = title;
	}
	
	public void editContent(String content) {
		this.content = content;
	}
	
	public void initMediaList() {
		this.mediaList.clear();
	}
	
	public void addHit() {
		this.hit += 1;
	}
	
	public void addRecommend() {
		this.recommend += 1;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	//연관관계메서드
	//멤버-게시글
	public void setWriter(Member writer) {
		this.writer = writer;
		writer.getPostList().add(this);
	}	
}
