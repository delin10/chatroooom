package nil.ed.chatroom.service;

import nil.ed.chatroom.common.PageResult;
import nil.ed.chatroom.common.Response;
import nil.ed.chatroom.entity.RoomEntity;
import nil.ed.chatroom.entity.UserEntity;
import nil.ed.chatroom.vo.UserChatRecordVO;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.List;
import java.util.Set;

/**
 * @author delin10
 * @since 2019/10/14
 **/
public interface IRoomService {
    /**
     * 添加房间
     * @param room
     */
    Response<Void> addRoom(RoomEntity room);

    /**
     * 删除房间
     *
     * @param id 房间ID
     * @param operator 操作人
     * @return 返回删除结果
     */
    Response<Void> deleteRoom(Long id, Long operator);

    /**
     * 查询房间列表
     *
     * @return 房间列表
     */
    Response<PageResult<RoomEntity>> listRooms();

    /**
     * 获取某房间发言排行榜的用户
     *
     *
     * @param roomId 房间id
     * @return 用户列表
     */
    Response<List<Object>> listRankUsersTop50(Long roomId);

    /**
     * 增加发言次数
     *
     * @param roomId roomId
     * @param userId 用户Id
     * @param delta 自增次数
     */
    void incrementChatRecord(Long roomId, Long userId, Long delta);

    /**
     * 检查房间id是否存在
     *
     * @param id 房间id
     * @return id是否存在
     */
    boolean checkExist(Long id);

    /**
     * 检查房间id的合法性
     *
     * @param message 消息
     * @param roomIdStr roomId字符串
     * @return roomId是否合法 true - 不合法
     */
    boolean validateRoomId(Message<?> message, String roomIdStr);

    /**
     * 解析room id
     *
     * @param accessor Stomp 首部访问
     * @return Room Id 字符串
     */
    String resolveRoomId(StompHeaderAccessor accessor);
}
