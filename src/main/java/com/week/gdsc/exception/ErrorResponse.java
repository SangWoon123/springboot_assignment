package com.week.gdsc.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    int status;
    String errorMessage;

    public ErrorResponse(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
