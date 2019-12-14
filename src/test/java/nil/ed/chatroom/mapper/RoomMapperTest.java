package nil.ed.chatroom.mapper;

import nil.ed.chatroom.AbstractServiceTest;
import nil.ed.chatroom.entity.RoomEntity;
import org.junit.Test;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author delin10
 * @since 2019/10/15
 **/
public class RoomMapperTest extends AbstractServiceTest {
    @Resource(name = "roomMapper")
    private RoomMapper roomMapper;

    @Test
    public void deleteByExampleTest(){
        Example example = new Example(RoomEntity.class);
        example.createCriteria().andEqualTo("id", 1);
        Integer count = roomMapper.deleteByExample(example);
        System.out.println("删除的条数为:" + count);
    }

}
