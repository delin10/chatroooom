package nil.ed.chatroom.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author delin10
 * @since 2019/10/14
 **/
@Data
@Table(name = "room")
public class RoomEntity extends TimeRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Long creator;

    private String description;
}
