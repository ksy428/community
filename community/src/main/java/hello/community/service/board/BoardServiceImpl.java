package hello.community.service.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.board.Board;
import hello.community.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService{
	
	private final BoardRepository boardRepository;
	
	@Override
	public Long write(String boardType) {
		
		Board board = Board.builder().boardType(boardType).build();
		
		boardRepository.save(board);
		
		return board.getId();
	}

}
