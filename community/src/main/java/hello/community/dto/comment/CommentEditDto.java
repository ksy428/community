package hello.community.dto.comment;

import java.util.List;

import javax.validation.constraints.NotBlank;

import hello.community.domain.comment.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentEditDto {

	@NotBlank(message = "내용을 입력하세요")
	private String content;
	
}
