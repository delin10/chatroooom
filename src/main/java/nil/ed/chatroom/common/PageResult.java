package nil.ed.chatroom.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    /*
    page No
     */
    private int pageNo;

    /*
    page Size
     */
    private int pageSize;

    /*
    total data count
     */
    private int total;

    /*
    data list
     */
    private List<T> data;

}
