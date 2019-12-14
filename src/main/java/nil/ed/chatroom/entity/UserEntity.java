package nil.ed.chatroom.entity;

import lombok.Data;

/**
 * @author delin10
 * @since 2019/10/14
 **/
@Data
public class UserEntity extends TimeRecordEntity{
    private Long id;

    private String name;
}
