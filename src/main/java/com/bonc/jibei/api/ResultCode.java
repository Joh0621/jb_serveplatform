package com.bonc.jibei.api;

/**
 * fgc
 * Created by renguangli at 2021/10/18 10:08 上午
 *
 * @since JDK1.8
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    NOT_FOUND(404, "NOT FOUND"),
    ;
    private final int code;
    private final String message;

    private ResultCode(int code, String message) {
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
