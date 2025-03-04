package com.example.authentication.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AppException extends RuntimeException{
    ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
