package nil.ed.chatroom.mapper;

import nil.ed.chatroom.entity.RoomEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author delin10
 * @since 2019/10/14
 **/
@Repository("roomMapper")
public interface RoomMapper extends Mapper<RoomEntity> {
}
