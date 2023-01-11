package hello.community.service.board;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.board.Board;
import hello.community.domain.member.Member;
import hello.community.domain.subscribe.Subscribe;
import hello.community.dto.board.BoardInfoDto;
import hello.community.exception.board.BoardException;
import hello.community.exception.board.BoardExceptionType;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.global.util.SecurityUtil;
import hello.community.repository.board.BoardRepository;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.subscribe.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.MemberRemoval;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService{
	
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;
	private final SubscribeRepository subscribeRepository;
	
	@Override
	@Transactional
	public Long write(String boardType) {
		
		Board board = Board.builder().boardType(boardType).build();
		
		boardRepository.save(board);
		
		return board.getId();
	}

	@Override
	public BoardInfoDto getInfo(String boardType) {
			
		Board board = boardRepository.findByBoardType(boardType)
				.orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));
		
		BoardInfoDto boardInfoDto = new BoardInfoDto(board);
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!principal.equals("anonymousUser")) {
			UserDetails loginMember = (UserDetails) principal;
			
			Member member = memberRepository.findByLoginId(loginMember.getUsername())
							.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
			
			if(subscribeRepository.findByMemberAndBoard(member, board).isPresent()) {
				boardInfoDto.setSubcribe(true);
			}
		}
					
		return boardInfoDto;
	}

	
}
