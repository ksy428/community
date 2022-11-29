package hello.community.domain.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	 public List<Comment> findRemovableList() {

	        List<Comment> result = new ArrayList<>();

	        Optional.ofNullable(this.parent).ifPresentOrElse(

	                parentComment ->{//대댓글인 경우 (부모가 존재하는 경우)
	                    if( parentComment.isDeleted()&& parentComment.isAllChildRemoved()){
	                        result.addAll(parentComment.getChildList());
	                        result.add(parentComment);
	                    }
	                },

	                () -> {//댓글인 경우
	                    if (isAllChildRemoved()) {
	                        result.add(this);
	                        result.addAll(this.getChildList());
	                    }
	                }
	        );

	        return result;
	    }


	    //모든 자식 댓글이 삭제되었는지 판단
	    private boolean isAllChildRemoved() {
	        return getChildList().stream()
	                .map(Comment::isDeleted)//지워졌는지 여부로 바꾼다
	                .filter(isRemove -> !isRemove)//지워졌으면 true, 안지워졌으면 false이다. 따라서 filter에 걸러지는 것은 false인 녀석들이고, 있다면 false를 없다면 orElse를 통해 true를 반환한다.
	                .findAny()//지워지지 않은게 하나라도 있다면 false를 반환
	                .orElse(true);//모두 지워졌다면 true를 반환

	    }
}
