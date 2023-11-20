package com.week.gdsc.aspect.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessLogicException e){
        ErrorCode errorCode = e.getErrorCode();

        // ErrorResponse는 에러 응답을 나타내는 dto클래스
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(), Collections.singletonList(errorCode.getMessage()));

        log.error("handleBusinessException: "+errorCode.getMessage());

        return new ResponseEntity(errorResponse,HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e){
        List<String> errorMessage=e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        ErrorResponse errorResponse=new ErrorResponse(401,errorMessage);
        return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.BAD_REQUEST);
    }




}
