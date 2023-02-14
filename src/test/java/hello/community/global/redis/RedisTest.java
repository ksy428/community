package hello.community.global.redis;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import hello.community.domain.board.Board;
import hello.community.dto.board.BoardInfoDto;
import hello.community.exception.board.BoardException;
import hello.community.exception.board.BoardExceptionType;
import hello.community.repository.board.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.member.Member;
import hello.community.global.util.DateUtil;
import hello.community.repository.member.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class RedisTest {
    @Autowired RedisService redisService;
    @Autowired BoardRepository boardRepository;
    private static List<String> boardList = new ArrayList<>(Arrays.asList("free","humor","info","soccer","baseball"));

    @Test
    void Redis조회테스트() throws Exception {

        for (int i = 0; i < 5; i++) {
            Instant instant = Instant.now();
            long timeStampMillis = instant.toEpochMilli();

            Board board = boardRepository.findByBoardType(boardList.get(i))
                    .orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));

            BoardInfoDto boardInfoDto = new BoardInfoDto(board);

            redisService.setRecentBoard("test", boardInfoDto, timeStampMillis);
        }

        List<BoardInfoDto> list = redisService.getRecentBoard("test");

        for (BoardInfoDto o : list) {
            log.info("내용물: {}", o.toString());
        }
        assertThat(list.size()).isEqualTo(5);

    }

    @Test
    void Redis방문한곳_중복조회() throws Exception{

        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();

        Board board = boardRepository.findByBoardType("info")
                .orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));

        BoardInfoDto boardInfoDto = new BoardInfoDto(board);

       redisService.setRecentBoard("test", boardInfoDto, timeStampMillis);

        List<BoardInfoDto> list = redisService.getRecentBoard("test");

        for(BoardInfoDto o : list){
            log.info("내용물: {}", o.toString() );
        }

        assertThat(list.size()).isEqualTo(5);
    }

    @Test
    void Redis방문한곳_삭제() throws Exception{

        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();

        Board board = boardRepository.findByBoardType("info")
                .orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));

        BoardInfoDto boardInfoDto = new BoardInfoDto(board);

        redisService.setRecentBoard("test", boardInfoDto, timeStampMillis);

        List<BoardInfoDto> list = redisService.getRecentBoard("test");

        for(BoardInfoDto o : list){
            log.info("전: {}", o.toString() );
        }

        redisService.deleteRecentBoard("test", "info");

        List<BoardInfoDto> list2 = redisService.getRecentBoard("test");

        for(BoardInfoDto o : list2){
            log.info("후: {}", o.toString() );
        }

        //assertThat(list.size()).isEqualTo(5);
    }
}
