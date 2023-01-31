package hello.community.dto.comment;


import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentEditDto {

	@NotBlank(message = "내용을 입력하세요")
	private String content;
	
}
