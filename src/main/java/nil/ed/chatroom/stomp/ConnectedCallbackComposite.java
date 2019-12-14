package nil.ed.chatroom.stomp;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author delin10
 * @since 2019/10/21
 **/
public class ConnectedCallbackComposite implements ConnectedCallback {
    private List<ConnectedCallback> connectionCallbackList = new ArrayList<>(4);

    public void setConnectionCallbackList(ConnectedCallback...connectionCallbacks) {
        Collections.addAll(connectionCallbackList, connectionCallbacks);
    }

    @Override
    public void onConnected(String roomId, Message<?> message) {
        for (ConnectedCallback connectedCallback : connectionCallbackList){
            connectedCallback.onConnected(roomId, message);
        }
    }
}
