package nil.ed.chatroom.service;

import nil.ed.chatroom.entity.TimeRecordEntity;

import java.sql.Time;
import java.time.Instant;

/**
 * @author delin10
 * @since 2019/10/15
 **/
public class AbstractService {
    protected void recordCreateAndUpdateTime(TimeRecordEntity entity){
        Long currentTimestamp = Instant.now().getEpochSecond();
        entity.setCreateTime(currentTimestamp);
        entity.setUpdateTime(currentTimestamp);
    }

    protected void recordUpdateTime(TimeRecordEntity entity){
        Long currentTimestamp = Instant.now().getEpochSecond();
        entity.setUpdateTime(currentTimestamp);
    }
}
