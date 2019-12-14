package nil.ed.chatroom.stomp.interceptor;

import lombok.extern.slf4j.Slf4j;
import nil.ed.chatroom.service.IRoomService;
import nil.ed.chatroom.stomp.IRejectStrategy;
import nil.ed.chatroom.util.Checker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author delin10
 * @since 2019/10/17
 **/
@Slf4j
public class StompMessageInterceptor implements ChannelInterceptor {
    @Resource(name = "roomService")
    private IRoomService roomService;

    private IRejectStrategy rejectStrategy;

    private Set<String> validPrefixes = new HashSet<>(4);

    private Set<String> userPrefixes = new HashSet<>(4);

    {
        validPrefixes.add("/topic/echo");
        validPrefixes.add("/topic/group");
        validPrefixes.add("/topic/oneToOne");

        userPrefixes.add("/user");
    }

    @Autowired
    public void setRejectStrategy(@Qualifier("simpleRejectStrategy") IRejectStrategy rejectStrategy) {
        this.rejectStrategy = rejectStrategy;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Message: {}", message);
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);
        Assert.notNull(accessor, "StompHeaderAccessor not null");

        StompCommand command = accessor.getCommand();
        if (StompCommand.SEND.equals(command)
                || StompCommand.SUBSCRIBE.equals(command)
                || StompCommand.UNSUBSCRIBE.equals(command)){
            String dest = accessor.getDestination();
            Assert.notNull(dest, "Destination not null");
            int dotLastIndex = dest.lastIndexOf(".");
            String prefix = dest.substring(0, dotLastIndex > 0 ? dotLastIndex : dest.length());



            if (!hasUserPrefix(dest) && !validPrefixes.contains(prefix)){
                rejectStrategy.reject(message, String.format("不合法的前缀: %s", prefix), channel);
                return null;
            }

            String roomId = null;
            if (dotLastIndex > 0){
                roomId = dest.substring( dotLastIndex + 1);
            }

            if (!roomService.validateRoomId(message, roomId)){
                rejectStrategy.reject(message, String.format("不合法的房间id: %s", roomId), channel);
                return null;
            }

            if (StompCommand.SEND.equals(command)){
                roomService.incrementChatRecord(Checker.checkPureLongNumber(roomId).longValue(), (long) accessor.getUser().getName().hashCode(), 1L);
            }
        }
        return message;
    }

    private boolean hasUserPrefix(String pattern){
        return userPrefixes.stream().
                anyMatch(pattern::startsWith);
    }
}
