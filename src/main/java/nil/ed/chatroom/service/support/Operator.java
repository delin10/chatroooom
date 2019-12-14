package nil.ed.chatroom.service.support;

import nil.ed.chatroom.common.Response;

import java.util.function.Supplier;

public interface Operator<T,R> {
    Response<T> operate(Supplier<R> supplier);
}
