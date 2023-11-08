package com.week.gdsc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class BusinessLogicException extends RuntimeException {

    private final ErrorCode errorCode;

}