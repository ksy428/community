package hello.community.domain.member;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import hello.community.domain.BaseTimeEntity;
import hello.community.domain.comment.Comment;
import hello.community.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String loginId;

	private String password;

	private String nickname;
	
	private String email;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> postList = new ArrayList<>();
	
	@OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> commentList = new ArrayList<>();
	
	@Builder
	public Member(String loginId, String password, String nickname, String email) {
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.email = email;
		this.role = Role.MEMBER;
	}
	
	public void editNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void editPassword(String password) {
		this.password = password;
	}
	
	public void editEmail(String email) {
		this.email = email;
	}
}
