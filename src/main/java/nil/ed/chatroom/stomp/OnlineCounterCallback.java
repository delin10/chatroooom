package nil.ed.chatroom.stomp;

import nil.ed.chatroom.common.RedisPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * @author delin10
 * @since 2019/10/21
 **/
public class OnlineCounterCallback implements ConnectedCallback {
    private StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onConnected(String roomId, Message<?> message) {
        redisTemplate.opsForValue().increment(String.format(RedisPrefix.ROOM_ONLINE_COUNT_PATTERN, roomId));
    }
}
