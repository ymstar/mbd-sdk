package com.mbdpay;

/**
 * 面包多Pay SDK 异常
 */
public class MbdPayException extends RuntimeException {

    private final String errorBody;

    public MbdPayException(String message) {
        super(message);
        this.errorBody = message;
    }

    public MbdPayException(String message, Throwable cause) {
        super(message, cause);
        this.errorBody = message;
    }

    public String getErrorBody() {
        return errorBody;
    }
}
