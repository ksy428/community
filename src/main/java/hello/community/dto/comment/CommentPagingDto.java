package hello.community.dto.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import hello.community.domain.comment.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
@NoArgsConstructor
@Slf4j
public class CommentPagingDto {

	// 총 페이지 개수
	private int totalPageCount;
	// 현재 페이지 번호
	private int currentPageNum;
	// 총 게시글의 개수
	private long totalElementCount;
	// 다음,이전 페이지 여부
	private boolean hasPrev, hasNext;
	// 시작 페이지 번호
	private int startPageNum;
	// 종료 페이지 번호
	private int endPageNum;
	// 페이지 버튼 개수
	private int pageNum;
	

	private List<CommentBriefInfo> commentList = new ArrayList<>();

	public CommentPagingDto(Page<Comment> searchResults) {

		
		totalPageCount = searchResults.getTotalPages();
		currentPageNum = searchResults.getNumber() + 1;
		totalElementCount = searchResults.getTotalElements();
		commentList = searchResults.getContent().stream().map(CommentBriefInfo::new).collect(Collectors.toList());
		
		pageNum = 10;
		
		// 현재페이지가 페이지버튼 개수 중간값 보다 작을 경우 ( ex: [1] ~ [현재페이지] ~ [10])
		if (currentPageNum <= pageNum / 2) {
			// 현재페이지와 총 페이지 개수를 이용해 페이지버튼 start,end 인덱스 부여
			startPageNum = 1;
			endPageNum = totalPageCount < pageNum ? totalPageCount : pageNum;

			if (endPageNum == 0) {
				endPageNum++;
			}

			hasPrev = false;
			hasNext = totalPageCount > pageNum;
		}
		// 현재페이지가 페이지버튼 개수 중간값보다 클 경우
		else {
			// (ex: [<<][<][5] ~ [현재페이지] ~ [마지막페이지] )
			if (currentPageNum + (pageNum / 2) >= totalPageCount) {
				startPageNum = totalPageCount < pageNum ? 1 : (totalPageCount - pageNum) + 1;
				endPageNum = totalPageCount;
				hasPrev = totalPageCount > pageNum;
				hasNext = false;
			}
			// (ex: [<<][<][5] ~ [현재페이지] ~ [14][>][>>] )
			else {
				startPageNum = (currentPageNum - (pageNum / 2)) + 1;
				endPageNum = (currentPageNum + (pageNum / 2));
				hasPrev = currentPageNum - (pageNum / 2) >= 1;
				hasNext = currentPageNum + (pageNum / 2) < totalPageCount;
			}
		}
		
		/*log.info("결과사이즈: {}", commentList.size());
		log.info("totalElementCount: {}", totalElementCount);
		log.info("current: {}", currentPageNum);
		log.info("start: {}", startPageNum);
		log.info("end: {}", endPageNum);
		log.info("hasPrev: {}", hasPrev);
		log.info("hasNext: {}", hasNext);*/
	}
}
