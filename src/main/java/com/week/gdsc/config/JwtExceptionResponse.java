package com.week.gdsc.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class JwtExceptionResponse {
    String message;
    HttpStatus status;

    public JwtExceptionResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
