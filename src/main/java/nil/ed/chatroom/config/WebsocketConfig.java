package nil.ed.chatroom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.user.UserDestinationMessageHandler;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author delin10
 * @since 2019/11/1
 **/
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CustomWebsocketHandler(), "/chat/web/socket");
    }

    private static class CustomWebsocketHandler extends TextWebSocketHandler {
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            System.out.println("receive:" + message.getPayload());
            session.sendMessage(new TextMessage("hello"));
            session.close(CloseStatus.GOING_AWAY);
        }
    }
}
