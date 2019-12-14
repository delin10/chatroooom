package nil.ed.chatroom.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Component;

/**
 * @author delin10
 * @since 2019/10/24
 **/
@Component
public class BrokerEventListener implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return BrokerAvailabilityEvent.class.equals(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        BrokerAvailabilityEvent brokerAvailabilityEvent = (BrokerAvailabilityEvent) event;
        if (!brokerAvailabilityEvent.isBrokerAvailable()){
            System.out.println(brokerAvailabilityEvent);
        }
    }
}
