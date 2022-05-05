package com.utils;

/**
 * @author zhengmh
 * @Description
 * @date 2021/01/01 2:07 PM
 * Modified By:
 */
enum ErrorCodeType {
    GENERAL,
}

public enum ErrorCode {
    UNKNOWN_ERROR(500, "SERVER UNKNOWN ERRROR {0}", ErrorCodeType.GENERAL),
    PAY_ERROR(404, "PAY ERRROR {0}", ErrorCodeType.GENERAL),
    NULL_POINTER(500, "NULL POINTER {0}", ErrorCodeType.GENERAL);

    long code;
    String message;
    ErrorCodeType errorCodeType;

    ErrorCode(long code, String message, ErrorCodeType errorCodeType) {
        this.code = code;
        this.message = message;
        this.errorCodeType = errorCodeType;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCodeType getErrorCodeType() {
        return errorCodeType;
    }
}
