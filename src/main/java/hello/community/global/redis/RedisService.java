package hello.community.global.redis;


import hello.community.dto.board.BoardInfoDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface RedisService {

     void setRecentBoard(String key, BoardInfoDto value,long timeStamp);

     List<BoardInfoDto> getRecentBoard(String key);

     void deleteRecentBoard(String key, String boardType);
}
