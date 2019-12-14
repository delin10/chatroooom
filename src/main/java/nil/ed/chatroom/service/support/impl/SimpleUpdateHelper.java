package nil.ed.chatroom.service.support.impl;

import nil.ed.chatroom.common.NormalResponseBuilder;
import nil.ed.chatroom.common.Response;
import nil.ed.chatroom.common.ResponseCodeEnum;
import nil.ed.chatroom.service.support.UpdateHelper;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component("simpleUpdateHelper")
public class SimpleUpdateHelper implements UpdateHelper {
    @Override
    public Response<Void> operate(Supplier<Integer> supplier)  {
        Integer updateResult = supplier.get();

        if (updateResult == 0){
            return new NormalResponseBuilder<Void>()
                    .setCodeEnum(ResponseCodeEnum.NOT_FOUND)
                    .build();
        }

        return new NormalResponseBuilder<Void>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }
}
