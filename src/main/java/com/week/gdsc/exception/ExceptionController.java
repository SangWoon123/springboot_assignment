package com.week.gdsc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessLogicException e){
        ErrorCode errorCode = e.getErrorCode();

        // ErrorResponse는 에러 응답을 나타내는 dto클래스
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());

        log.error("handleBusinessException: "+errorCode.getMessage());

        return new ResponseEntity(errorResponse,HttpStatus.valueOf(errorCode.getStatus()));
    }

}
