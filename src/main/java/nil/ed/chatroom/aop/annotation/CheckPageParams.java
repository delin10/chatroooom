package nil.ed.chatroom.aop.annotation;

import nil.ed.chatroom.aop.PageParamsCheckExceptionHandler;

import java.lang.annotation.*;

/**
 * @author delin10
 * @since 2019/10/21
 **/
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckPageParams {
    String pageNoSignatureName();

    String pageSizeSignatureName();

    int minPageNo() default 0;

    int maxPageNo() default Integer.MAX_VALUE;

    int maxPageSize() default 20;

    Class<? extends PageParamsCheckExceptionHandler> handler();
}
