package hello.community.dto.comment;

import java.time.LocalDateTime;

import hello.community.domain.comment.Comment;
import hello.community.domain.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CommentBriefInfo {

	private Long commentId;
	private String content;
	private String writerNickname;
	private LocalDateTime createdDate;
	
	public CommentBriefInfo(Comment comment) {
		this.commentId = comment.getId();
		this.content = comment.getContent();
		this.writerNickname = comment.getWriter().getNickname();
		this.createdDate = comment.getCreatedDate();
	}
}
