package nil.ed.chatroom.stomp;

import nil.ed.chatroom.common.RedisPrefix;
import nil.ed.chatroom.util.stomp.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Optional;

/**
 * @author delin10
 * @since 2019/10/21
 **/
public class EchoCallback implements ConnectedCallback {
    private final String ECHO_PATTERN = "%s加入房间，当前房间人数为:%s人";
    private SimpMessagingTemplate simpMessagingTemplate;
    private StringRedisTemplate stringRedisTemplate;
    private ValueOperations<String, String> valueOperations;

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.valueOperations = stringRedisTemplate.opsForValue();
    }

    @Override
    public void onConnected(String roomId, Message<?> message) {
        StompHeaderAccessor accessor = MessageUtils.getStompHeaderAccessor(message);
        Assert.notNull(accessor, "accessor not null");
        StompHeaderAccessor header = StompHeaderAccessor.create(StompCommand.MESSAGE);
        header.setSessionId(accessor.getSessionId());
        String onlineCountKey = String.format(RedisPrefix.ROOM_ONLINE_COUNT_PATTERN, roomId);
        String name = Optional.ofNullable(accessor.getUser()).map(Principal::getName).orElse("unknown");
        String payload = String.format(ECHO_PATTERN, name, valueOperations.get(onlineCountKey));
        simpMessagingTemplate.convertAndSend("/topic/echo." + roomId, payload);
    }
}
