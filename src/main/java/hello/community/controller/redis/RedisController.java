package hello.community.controller.redis;

import hello.community.domain.board.Board;
import hello.community.domain.member.Member;
import hello.community.dto.board.BoardInfoDto;
import hello.community.dto.subscribe.SubscribeInfoDto;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/redis/recent-board")
    public ResponseEntity<List<BoardInfoDto>> recentBoard(){

        List<BoardInfoDto> recnetBoardList = new ArrayList<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!principal.equals("anonymousUser")) {
            UserDetails loginMember = (UserDetails) principal;

            recnetBoardList = redisService.getRecentBoard(loginMember.getUsername());
        }

        return new ResponseEntity<>(recnetBoardList,HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/redis/recent-board/delete/{boardType}")
    public void deleteRecentBoard(@PathVariable String boardType){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!principal.equals("anonymousUser")) {
            UserDetails loginMember = (UserDetails) principal;

            //최근방문한게시판 삭제
            redisService.deleteRecentBoard(loginMember.getUsername(), boardType);
        }
    }
}
