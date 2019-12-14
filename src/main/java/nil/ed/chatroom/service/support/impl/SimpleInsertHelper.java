package nil.ed.chatroom.service.support.impl;

import nil.ed.chatroom.common.NormalResponseBuilder;
import nil.ed.chatroom.common.Response;
import nil.ed.chatroom.service.support.InsertHelper;

import java.util.function.Supplier;

public class SimpleInsertHelper implements InsertHelper {
    @Override
    public Response<Void> operate(Supplier<Void> supplier) {
        supplier.get();

        return new NormalResponseBuilder<Void>()
                .success(null);
    }
}
