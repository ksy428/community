package hello.community.dto.post;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.media.Media;
import hello.community.domain.post.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostEditDto {

	@NotBlank(message = "제목을 입력하세요")
	private String title;
	
	@NotBlank(message = "내용을 입력하세요")
	private String content;
	
	private List<MultipartFile> newUpdoaldFile = new ArrayList<>();
	
	private List<Media> uploadMedia = new ArrayList<>();

	public PostEditDto(Post post) {
		this.title = post.getTitle();
		this.content = post.getContent();
		this.uploadMedia = post.getMediaList();
	}
}
