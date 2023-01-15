package hello.community.repository.subscribe;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import hello.community.domain.board.Board;
import hello.community.domain.member.Member;
import hello.community.domain.subscribe.Subscribe;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long>{

	// 지금은 writer, board 접근안해서 패치조인 안 해도 될듯?
	//@EntityGraph(attributePaths = {"writer","board"})
	Optional<Subscribe> findByWriterAndBoard(Member writer, Board board);
}
