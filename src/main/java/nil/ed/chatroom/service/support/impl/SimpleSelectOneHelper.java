package nil.ed.chatroom.service.support.impl;

import nil.ed.chatroom.common.NormalResponseBuilder;
import nil.ed.chatroom.common.Response;
import nil.ed.chatroom.common.ResponseCodeEnum;
import nil.ed.chatroom.service.support.SelectOneHelper;

import java.util.Objects;
import java.util.function.Supplier;

public class SimpleSelectOneHelper<T> implements SelectOneHelper<T> {
    @Override
    public Response<T> operate(Supplier<T> supplier) {
        T result = supplier.get();

        if (Objects.isNull(result)){
            return new NormalResponseBuilder<T>()
                    .setCodeEnum(ResponseCodeEnum.NOT_FOUND)
                    .build();
        }
        return new NormalResponseBuilder<T>()
                .setData(result)
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }
}
