package com.example.authentication.exception;

public enum ErrorCode {
    TOKEN_INVALID("TOKEN_00",501, "Token invalid"),
    TOKEN_EXPIRED("TOKEN_01",501, "Token is expired"),
    SIGNATURE_INVALID("TOKEN_02",501, "Sigature is not valid"),

    USER_NOT_EXIST("USER_01", 501, "Username is not correct"),
    PASSWORD_INCORRECT("USER_02",501, "Password is not correct"),
    USER_EXISTED("USER_01", 501, "User was exist");

    private String code;
    private int httpStatus;
    private String message;

    ErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
