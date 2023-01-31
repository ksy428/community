package hello.community.controller.board;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import hello.community.dto.board.BoardInfoDto;
import hello.community.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

	private final BoardService boardService;
	
	@GetMapping("/boards/{boardType}")
	public ResponseEntity<BoardInfoDto> boardInfo(@PathVariable String boardType){
			
		BoardInfoDto boardInfoDto = boardService.getInfo(boardType);
				
		return new ResponseEntity<>(boardInfoDto,HttpStatus.OK);
	}
	
}
