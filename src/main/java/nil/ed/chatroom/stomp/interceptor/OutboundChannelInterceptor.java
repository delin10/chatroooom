package nil.ed.chatroom.stomp.interceptor;

import lombok.extern.slf4j.Slf4j;
import nil.ed.chatroom.service.IRoomService;
import nil.ed.chatroom.stomp.ConnectedCallback;
import nil.ed.chatroom.util.sensitive.SensitiveFilter;
import nil.ed.chatroom.util.stomp.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

/**
 * @author delin10
 * @since 2019/10/24
 **/
@Slf4j
public class OutboundChannelInterceptor implements ChannelInterceptor {

    private SensitiveFilter sensitiveFilter;

    private IRoomService roomService;

    private ConnectedCallback connectedCallback;

    public void setConnectedCallback(ConnectedCallback connectedCallback) {
        this.connectedCallback = connectedCallback;
    }

    @Autowired
    public void setRoomService(@Qualifier("roomService") IRoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public void setSensitiveFilter(SensitiveFilter sensitiveFilter) {
        this.sensitiveFilter = sensitiveFilter;
    }

    @Override

    public Message<?> preSend( Message<?> message, MessageChannel channel) {
        message = MessageUtils.setStompHeaderAccessor(message);
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.info(String.valueOf(accessor.getMessageHeaders()));
        Assert.notNull(accessor, "Accessor cannot be null");

        if (StompCommand.MESSAGE.equals(accessor.getCommand())){
            String payload = MessageUtils.byteArrayPayloadToString(message);
            String markPayload = sensitiveFilter.filter(payload).getMaskText();
            return MessageBuilder.createMessage(markPayload.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
        }

        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        if (!sent){
            return;
        }

        log.info("Succeed to send message: {}", message);
        StompHeaderAccessor accessor = MessageUtils.getStompHeaderAccessor(message);
        Assert.notNull(accessor, "StompHeaderAccessor not null");

        //在preSend时还没有建立连接, 所以那个时候无法发送到消息代理中导致消息丢失
        StompCommand command = accessor.getCommand();
        log.info("Handshake response: {}", command);

        if (StompCommand.CONNECTED.equals(command)){
            String roomIdStr = roomService.resolveRoomId(accessor);
            connectedCallback.onConnected(roomIdStr, message);
        }
    }
}
