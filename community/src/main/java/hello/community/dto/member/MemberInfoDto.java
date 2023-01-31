package hello.community.dto.member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import hello.community.domain.comment.Comment;
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.dto.comment.CommentBriefInfo;
import hello.community.dto.post.PostBriefInfo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoDto {

	private String loginId;
	private String nickname;
	private String email;
	private LocalDateTime createdDate;
	private List<PostBriefInfo> postList = new ArrayList<>();
	private List<CommentBriefInfo> commentList = new ArrayList<>();
	
	public MemberInfoDto(Member member) {
		this.loginId = member.getLoginId();
		this.nickname = member.getNickname();
		this.email = member.getEmail();
		this.createdDate = member.getCreatedDate();
		this.postList = member.getPostList().stream().sorted(Comparator.comparing(Post::getCreatedDate).reversed()).limit(10)
				.map(PostBriefInfo::new).toList();
		this.commentList = member.getCommentList().stream().sorted(Comparator.comparing(Comment::getCreatedDate).reversed()).limit(10)
				.map(CommentBriefInfo::new).toList();		
	}
}
