package nil.ed.chatroom.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author delin10
 * @since 2019/10/18
 **/
@Component("b")
public class BBean {
    private ABean a;

    @Autowired
    public void setA(ABean a) {
        this.a = a;
    }
}
