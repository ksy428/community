package hello.community.dto.post;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.util.CollectionUtils;
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
	
	private String boardType;

	private List<Media> mediaList = new ArrayList<Media>();

	@Builder
	public PostWriteDto(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void initDto(List<String> originNameList, List<String> storeNameList, String boardType) {
	
		this.boardType = boardType;
		
		if(CollectionUtils.isEmpty(storeNameList))
			return;
		
		for (int i = 0; i < storeNameList.size(); i++) {
			//summernote에서 tmp파일 생성 후 최종 삭제한 경우 필터링
			if (content.contains(storeNameList.get(i))) {
				Media media = Media.builder()
						.originName(originNameList.get(i))
						.storeName(storeNameList.get(i))
						.build();
				mediaList.add(media);
			}
		}

		content = content.replace("/tmp", "/file");
	}

	public Post toEntity() {
		return Post.builder().title(this.title).content(this.content).build();
	}
}
