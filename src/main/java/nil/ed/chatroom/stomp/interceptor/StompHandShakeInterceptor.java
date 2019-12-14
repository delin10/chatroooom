package nil.ed.chatroom.stomp.interceptor;

import lombok.extern.slf4j.Slf4j;
import nil.ed.chatroom.service.IRoomService;
import nil.ed.chatroom.stomp.ConnectedCallback;
import nil.ed.chatroom.stomp.IRejectStrategy;
import nil.ed.chatroom.util.NameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @author delin10
 * @since 2019/10/17
 **/
@Slf4j
public class StompHandShakeInterceptor implements ChannelInterceptor {
    @Resource(name = "roomService")
    private IRoomService roomService;

    private IRejectStrategy rejectStrategy;

    @Autowired
    public void setRejectStrategy(@Qualifier("simpleRejectStrategy") IRejectStrategy rejectStrategy) {
        this.rejectStrategy = rejectStrategy;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);
        Assert.notNull(accessor, "StompHeaderAccessor not null");
        StompCommand command = accessor.getCommand();
        if (!StompCommand.CONNECT.equals(command)){
            return message;
        }

        String roomIdStr = roomService.resolveRoomId(accessor);
        if (!roomService.validateRoomId(message, roomIdStr)){
            rejectStrategy.reject(message, String.format("不合法的房间id: %s", roomIdStr), channel);
            return null;
        }
        if (accessor.getUser() == null) {
            String name = NameGenerator.generate();
            accessor.setUser(() -> name);
        }

        return message;
    }
}
