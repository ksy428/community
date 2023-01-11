package hello.community.repository.subscribe;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.community.domain.board.Board;
import hello.community.domain.member.Member;
import hello.community.domain.subscribe.Subscribe;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long>{

	Optional<Subscribe> findByMemberAndBoard(Member member, Board board);
}
