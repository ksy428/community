package hello.community.dto.comment;

import javax.validation.constraints.NotBlank;

import hello.community.domain.comment.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentWriteDto {

	@NotBlank(message = "내용을 입력하세요")
	private String content;
	
	public Comment toEntity() {
		return Comment.builder().content(this.content).build();
	}
}
