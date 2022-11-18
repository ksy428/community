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
public class PostWriteDto {

	@NotBlank(message = "제목을 입력하세요")
	private String title;
	
	@NotBlank(message = "내용을 입력하세요")
	private String content;
	
	private List<Media> mediaList;
	
	//private List<Media> uploadMedia = new ArrayList<>();
	
	private List<MultipartFile> uploadFile = new ArrayList<>();
	@Builder
	public PostWriteDto(String title,String content, List<MultipartFile> uploadFile) {
		this.title = title;
		this.content = content;
		this.uploadFile = uploadFile;
	}
	
	public Post toEntity() {
		return Post.builder()
				.title(this.title)
				.content(this.content)
				.build();				
	}
}
