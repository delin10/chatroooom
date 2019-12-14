package nil.ed.chatroom.service.impl;

import lombok.extern.slf4j.Slf4j;
import nil.ed.chatroom.common.NormalResponseBuilder;
import nil.ed.chatroom.common.PageResult;
import nil.ed.chatroom.common.RedisPrefix;
import nil.ed.chatroom.common.Response;
import nil.ed.chatroom.entity.RoomEntity;
import nil.ed.chatroom.entity.UserEntity;
import nil.ed.chatroom.mapper.RoomMapper;
import nil.ed.chatroom.service.AbstractService;
import nil.ed.chatroom.service.IRoomService;
import nil.ed.chatroom.service.MessageIncompleteException;
import nil.ed.chatroom.service.support.impl.SimpleDeleteHelper;
import nil.ed.chatroom.service.support.impl.SimpleSelectOneHelper;
import nil.ed.chatroom.service.support.impl.SimpleSelectPageHelper;
import nil.ed.chatroom.util.Checker;
import nil.ed.chatroom.vo.UserChatRecordVO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author delin10
 * @since 2019/10/14
 **/
@Slf4j
@Service("roomService")
public class RoomServiceImpl extends AbstractService implements IRoomService {
    @Resource(name = "roomMapper")
    private RoomMapper roomMapper;

    private ZSetOperations<String, Object> zSetOperations;

    private Executor databaseQueryExecutor;

    @Autowired
    public void setDatabaseQueryExecutor(@Qualifier("databaseQueryExecutor") Executor databaseQueryExecutor) {
        this.databaseQueryExecutor = databaseQueryExecutor;
    }

    @Autowired
    public void setValueOperations(RedisTemplate<String, Object> objectRedisTemplate) {
        this.zSetOperations = objectRedisTemplate.opsForZSet();
    }

    @Override
    public Response<Void> addRoom(RoomEntity room) {
        recordCreateAndUpdateTime(room);
        roomMapper.insert(room);
        return new NormalResponseBuilder<Void>()
                .success(null);
    }

    @Override
    public Response<Void> deleteRoom(Long id, Long operator) {
        return new SimpleDeleteHelper()
                .operate(() -> {
                    Example example = Example
                            .builder(RoomEntity.class)
                            .build();
                    example.createCriteria()
                    .andEqualTo("id", id)
                    .andEqualTo("creator", operator);
                    return roomMapper.deleteByExample(example);
                });
    }

    @Override
    public Response<PageResult<RoomEntity>> listRooms() {
        final int start = 0, size = Integer.MAX_VALUE;
        return new SimpleSelectPageHelper<RoomEntity>(databaseQueryExecutor)
                .setPageNo(start)
                .setPageSize(size)
                .setCounter(() -> roomMapper.selectCount(new RoomEntity()))
                .operate(() -> roomMapper.selectAll());
    }

    @Override
    public Response<List<Object>> listRankUsersTop50(Long roomId) {
        final String key = String.format(RedisPrefix.ROOM_CHAT_COUNT_OF_USER_PATTERN, roomId);
        return new SimpleSelectOneHelper<List<Object>>()
                .operate(() -> unwrapTypedTuple(zSetOperations.reverseRangeWithScores(key, 0, 50)));
    }

    @Override
    public void incrementChatRecord(Long roomId, Long userId, Long delta) {
        final String key = String.format(RedisPrefix.ROOM_CHAT_COUNT_OF_USER_PATTERN, roomId);
        zSetOperations.incrementScore(key, userId, delta);
    }

    @Override
    public boolean checkExist(Long id) {
        return roomMapper.existsWithPrimaryKey(id);
    }

    @Override
    public boolean validateRoomId(Message<?> message, String roomIdStr) {
        log.info("--->validateRoomId, roomId={}", roomIdStr);
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);
        Assert.notNull(accessor, "StompHeaderAccessor not null");

        Number roomIdNumber = null;
        if (roomIdStr != null){
            roomIdNumber = Checker.checkPureLongNumber(roomIdStr);
        }

        return !(roomIdNumber == null
                || !checkExist(roomIdNumber.longValue()));
    }

    @Override
    public String resolveRoomId(StompHeaderAccessor accessor) throws MessageIncompleteException{
        String roomIdStr = Optional.ofNullable(accessor.getNativeHeader("room-id"))
                .filter(ls -> !ls.isEmpty())
                .map(ls -> ls.get(0))
                .orElseGet(() -> {
                    String destination = accessor.getFirstNativeHeader(StompHeaderAccessor.DESTINATION_HEADER);
                    if (destination == null){
                        throw new MessageIncompleteException("Invalid header: loss destination");
                    }
                    int dotLastIndex = Optional.of(destination.lastIndexOf("."))
                            .filter(index -> index > 0)
                            .orElseThrow(() -> new MessageIncompleteException("Invalid header: there is no room id in destination!"));

                    String roomStr = destination.substring(dotLastIndex);
                    accessor.setNativeHeader("room-id", roomStr);
                    return roomStr;
                });
        log.info("resolved room id: {}", roomIdStr);
        return roomIdStr;
    }

    private List<Object> unwrapTypedTuple(Set<ZSetOperations.TypedTuple<Object>> typedTuples){
        return Optional.ofNullable(typedTuples)
                .map(set -> set.stream()
                        .map(this::mapToUserChatRecordVO)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private Object mapToUserChatRecordVO(ZSetOperations.TypedTuple<Object> typedTuple){
        UserChatRecordVO userChatRecordVO = new UserChatRecordVO();
        userChatRecordVO.setUsername(typedTuple.getValue()+"");
        userChatRecordVO.setChatCount(Optional.ofNullable(typedTuple.getScore()).map(Double::longValue).orElse(0L));
        return userChatRecordVO;
    }
}
