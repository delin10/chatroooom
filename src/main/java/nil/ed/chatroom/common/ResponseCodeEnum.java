package nil.ed.chatroom.common;

/**
 * @author delin10
 */
public enum ResponseCodeEnum {
    /*
    成功码
     */
    SUCCESS(0, "success"),
    FAILED(-1, "failed"),
    NOT_FOUND(1, "not found"),
    TIMEOUT(2, "async timeout"),
    DUPLICATE_OPERATION(3, " duplicate operation"),
    ACCESS_DENIED(998, "Access Denied"),
    UNCAUGHT_EXCEPTION(999, "uncaught exception");
    private int code;

    private String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
