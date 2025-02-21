package com.example.eroom.domain.chat.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {

        log.error("예외 발생: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {

        log.error("알 수 없는 오류 발생: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(ErrorCode.PROJECT_NOT_FOUND.getStatus(), "서버 오류가 발생했습니다.");
        return ResponseEntity.status(ErrorCode.PROJECT_NOT_FOUND.getStatus()).body(response);
    }
}
