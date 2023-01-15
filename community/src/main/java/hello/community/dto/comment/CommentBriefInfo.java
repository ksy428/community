package hello.community.dto.comment;


import java.time.format.DateTimeFormatter;

import hello.community.domain.comment.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CommentBriefInfo {

	private Long commentId;
	private String content;
	private String writerLoginId;
	private String writerNickname;
	private String createdDate;
	private Long parentId;
	private Boolean isDeleted;
	
	public CommentBriefInfo(Comment comment) {
		this.commentId = comment.getId();
		this.content = comment.getContent();
		this.writerLoginId = comment.getWriter().getLoginId();
		this.writerNickname = comment.getWriter().getNickname();
		this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.parentId = comment.getParent() == null ? null :comment.getParent().getId();
		this.isDeleted = comment.isDeleted();
	}
}
