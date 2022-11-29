package hello.community.dto.comment;

import java.util.ArrayList;
import java.util.List;

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
	boolean hasPrev, hasNext;
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
		commentList = searchResults.getContent().stream().map(CommentBriefInfo::new).toList();
		
		log.info("결과사이즈: {}", commentList.size());
		log.info("totalElementCount: {}", totalElementCount);
		log.info("댓글 리스트: {}", commentList.toString());
	}
}
