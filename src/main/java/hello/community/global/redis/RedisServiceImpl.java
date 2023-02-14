package hello.community.global.redis;

import hello.community.domain.board.Board;
import hello.community.dto.board.BoardInfoDto;
import hello.community.exception.board.BoardException;
import hello.community.exception.board.BoardExceptionType;
import hello.community.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService{
    @Autowired
    private final RedisTemplate redisTemplate;
    @Autowired
    private final BoardRepository boardRepository;
    public static final String REDIS_PREFIX_KEY = "redis::";
    public static final Long BOARD_MAX_SIZE = 10L;


    @Override
    public void setRecentBoard(String key, BoardInfoDto value, long timeStamp){
        ZSetOperations<String, BoardInfoDto> zSetOperations = redisTemplate.opsForZSet();

        zSetOperations.add(REDIS_PREFIX_KEY + key, value ,timeStamp);

        zSetOperations.removeRange(REDIS_PREFIX_KEY + key, -(BOARD_MAX_SIZE + 1), -(BOARD_MAX_SIZE + 1));

    }

    @Override
    public List<BoardInfoDto> getRecentBoard(String key){
        ZSetOperations<String, BoardInfoDto> zSetOperations = redisTemplate.opsForZSet();

        return new ArrayList<>(Objects.requireNonNull(
                zSetOperations.reverseRange(REDIS_PREFIX_KEY + key, 0, -1)));
    }

    @Override
    public void deleteRecentBoard(String key, String boardType) {
        ZSetOperations<String, BoardInfoDto> zSetOperations = redisTemplate.opsForZSet();

        Board board = boardRepository.findByBoardType(boardType)
                .orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));

        BoardInfoDto boardInfoDto = new BoardInfoDto(board);


        zSetOperations.remove(REDIS_PREFIX_KEY + key, boardInfoDto);
    }
}
