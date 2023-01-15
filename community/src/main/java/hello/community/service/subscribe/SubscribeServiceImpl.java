package hello.community.service.subscribe;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.board.Board;
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.domain.recommend.Recommend;
import hello.community.domain.subscribe.Subscribe;
import hello.community.exception.board.BoardException;
import hello.community.exception.board.BoardExceptionType;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.exception.post.PostException;
import hello.community.exception.post.PostExceptionType;
import hello.community.global.util.SecurityUtil;
import hello.community.repository.board.BoardRepository;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import hello.community.repository.recommend.RecommendRepository;
import hello.community.repository.subscribe.SubscribeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscribeServiceImpl implements SubscribeService{

	private final SubscribeRepository subscribeRepository;
	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;
	
	
	@Override
	@Transactional
	public void write(String boardType){
		
		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
			.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
		
		Board board = boardRepository.findByBoardType(boardType)
			.orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));
		
		Optional<Subscribe> result = subscribeRepository.findByWriterAndBoard(member, board);
		
		if (result.isEmpty()) {
			Subscribe subscribe = Subscribe.builder().writer(member).board(board).build();

			subscribeRepository.save(subscribe);
	
		}
		else {
			throw new BoardException(BoardExceptionType.OVERLAP_SUBSCRIBE_BOARD);
		}
	}


	@Override
	@Transactional
	public void delete(String boardType) {
		
		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
			
		Board board = boardRepository.findByBoardType(boardType)
				.orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));
		
		Subscribe result = subscribeRepository.findByWriterAndBoard(member, board)
				.orElseThrow(() -> new BoardException(BoardExceptionType.NOT_SUBSCRIBE_BOARD));
		
		subscribeRepository.delete(result);
		member.getSubscribeList().remove(result);
	}
	
	
}
