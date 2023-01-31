package hello.community.domain.comment;

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
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
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
	
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private List<Comment> childList = new ArrayList<>();
	
	private boolean isDeleted = false;
	
	private Long groupId;
	
	@Builder
	public Comment(String content) {
		this.content = content;
	}
	
	public void editContent(String content) {
		this.content = content;
	}
	
	public void editDeleteStatus() {
		this.isDeleted = true;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
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
	public void setParent(Comment parent) {
		this.parent = parent;
		parent.getChildList().add(this);
	}	
}
