package hello.community.service.board;

import hello.community.dto.board.BoardInfoDto;

public interface BoardService {

	Long write(String boardType);
	
	BoardInfoDto getInfo(String boardType);
}
