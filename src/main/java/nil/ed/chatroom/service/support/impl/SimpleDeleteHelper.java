package nil.ed.chatroom.service.support.impl;

import nil.ed.chatroom.common.NormalResponseBuilder;
import nil.ed.chatroom.common.Response;
import nil.ed.chatroom.common.ResponseCodeEnum;
import nil.ed.chatroom.service.support.DeleteHelper;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component("simpleDeleteHelper")
public class SimpleDeleteHelper implements DeleteHelper {
    @Override
    public Response<Void> operate(Supplier<Integer> supplier) {
        Integer deleteResult = supplier.get();

        if (deleteResult == 0){
            return new NormalResponseBuilder<Void>()
                    .setCodeEnum(ResponseCodeEnum.NOT_FOUND)
                    .build();
        }

        return new NormalResponseBuilder<Void>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }
}
