package nil.ed.chatroom.entity;

import lombok.Data;

import javax.persistence.Column;

/**
 * 更新时间和创建时间
 *
 * @author delin10
 * @since 2019/10/14
 **/
@Data
public class TimeRecordEntity {

    private Long updateTime;

    private Long createTime;
}
