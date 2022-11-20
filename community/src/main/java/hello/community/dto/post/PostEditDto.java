package hello.community.dto.post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.hibernate.transform.ToListResultTransformer;
import org.springframework.util.CollectionUtils;
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
	
	//private List<MultipartFile> newUpdoaldFile = new ArrayList<>();
	
	private List<Media> mediaList = new ArrayList<>();
	
	private List<Media> newMediaList = new ArrayList<>();
	
	private List<Media> deleteMediaList = new ArrayList<>();

	public void inItDto(List<String> originNameList, List<String> storeNameList)
	{
		//content내부에 List<Media> mediaList 저장이름들이 존재하는지 체크해서 삭제된거있는지 체크
		deleteMediaList = mediaList.stream()
				.filter(t -> !content.contains(t.getStoreName()))
				.collect(Collectors.toList());

		if(CollectionUtils.isEmpty(storeNameList))
			return;
	
		for (int i = 0; i < storeNameList.size(); i++) {
			//summernote에서 tmp파일 생성 후 최종 삭제한 경우 필터링
			if (content.contains(storeNameList.get(i))) {
				Media media = Media.builder()
						.originName(originNameList.get(i))
						.storeName(storeNameList.get(i))
						.build();
				newMediaList.add(media);
			}
		}
		content = content.replace("/tmp", "/file");		
	}
	public PostEditDto(Post post) {
		this.title = post.getTitle();
		this.content = post.getContent();
		this.mediaList = post.getMediaList();
	}
}
