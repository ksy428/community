package hello.community.repository.board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.community.domain.board.Board;


public interface BoardRepository extends JpaRepository<Board, Long>{

	Optional<Board> findByBoardType(String boardType);
}
