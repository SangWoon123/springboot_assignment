package com.week.gdsc.aspect.exception;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorResponse {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final List<String> errorMessage;
    public ErrorResponse(int status, List<String> errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
