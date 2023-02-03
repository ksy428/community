package hello.community.service.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import hello.community.domain.post.Post;

public interface MessageService {

    void sendMessage(String message);
}
