package hello.community.dto.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import hello.community.domain.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PostPagingDto {

	// 총 페이지 개수
	private int totalPageCount;
	// 현재 페이지 번호
	private int currentPageNum;
	// 총 게시글의 개수
	private long totalElementCount;
	// 다음,이전 페이지 여부
	boolean hasPrev, hasNext;
	// 시작 페이지 번호
	private int startPageNum;
	// 종료 페이지 번호
	private int endPageNum;
	// 페이지 버튼 개수
	private int pageNum;
	
	

	private List<BriefPostInfo> postList = new ArrayList<>();

	public PostPagingDto(Page<Post> searchResults) {

		totalPageCount = searchResults.getTotalPages();
		currentPageNum = searchResults.getNumber() + 1;
		totalElementCount = searchResults.getTotalElements();
		postList = searchResults.getContent().stream().map(BriefPostInfo::new).toList();

		pageNum = 10;
		// 현재페이지가 페이지버튼 개수 중간값 보다 작을 경우
		if(currentPageNum <= pageNum / 2) {
			// 현재페이지와 총 페이지 개수를 이용해 페이지버튼 start,end 인덱스 부여
			startPageNum = 1;
			endPageNum = totalPageCount < pageNum ? totalPageCount : pageNum;
			
			if(endPageNum == 0) {
				endPageNum++;
			}
			
			hasPrev = false;
			hasNext = totalPageCount > pageNum;			 
		}
		// 현재페이지가 페이지버튼 개수 중간값보다 클 경우
		else { 
			startPageNum = (currentPageNum - (pageNum / 2)) + 1;
			endPageNum = (currentPageNum + (pageNum / 2)) < totalPageCount ? (currentPageNum + (pageNum / 2)) : totalPageCount;
			hasPrev =  currentPageNum - (pageNum / 2) >= 1;
			hasNext = currentPageNum + (pageNum / 2) < totalPageCount;
		}	
	}
}
