package nil.ed.chatroom.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author delin10
 * @since 2019/10/18
 **/
@Component("a")
public class ABean {
    private BBean b;
    @Autowired
    public void setB(BBean b) {
        this.b = b;
    }
}