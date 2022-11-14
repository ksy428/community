package hello.community.domain.comment;

import java.util.ArrayList;
import java.util.List;

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
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity{

	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member writer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")	
	private Comment parent;
	
	@OneToMany(mappedBy = "parent")
	private List<Comment> child = new ArrayList<>();
	
	
	//연관관계 메서드
	//멤버-댓글
	public void setMember(Member writer) {
		this.writer = writer;
		writer.getCommentList().add(this);
	}
	//게시글-댓글
	public void setPost(Post post) {
		this.post = post;
		post.getCommentList().add(this);
	}
	
	//댓글-댓글
	public void addChildComment(Comment child) {
		this.child.add(child);
		child.parent = this;
	}
	
}
