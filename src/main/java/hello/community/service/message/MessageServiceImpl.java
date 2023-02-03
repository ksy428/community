package hello.community.service.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.community.domain.post.Post;
import hello.community.dto.post.PostBriefInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;
    @Override
    public void sendMessage(String message){

        log.info("알림: {}", message);
        brokerMessagingTemplate.convertAndSend("/topic/main",message);
    }
}
