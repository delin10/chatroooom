package nil.ed.chatroom.common;

/**
 * @author delin10
 * @since 2019/10/21
 **/
public class RedisPrefix {
    public static final String ROOM_ONLINE_COUNT_PATTERN = "room_online_count:room_id_%s";

    /**
     * 聊天室的用户发言统计
     */
    public static final String ROOM_CHAT_COUNT_OF_USER_PATTERN = "room_chat_count:room_id_%s";
}
