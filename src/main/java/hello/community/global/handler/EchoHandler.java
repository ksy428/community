package hello.community.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EchoHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> users = new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String id = session.getId();

        users.add(session);

        log.info("웹소켓 연결ID: {}", id);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        log.info("payload : " + payload);

        for(WebSocketSession user : users){
            user.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String id = session.getId();

        log.info("웹소켓 연결 해제ID: {}", id);

        users.remove(session);
    }
}
