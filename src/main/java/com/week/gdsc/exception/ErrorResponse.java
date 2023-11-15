package com.week.gdsc.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final List<String> errorMessage;
    public ErrorResponse(int status, List<String> errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
