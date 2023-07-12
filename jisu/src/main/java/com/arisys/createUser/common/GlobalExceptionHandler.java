package com.arisys.createUser.common;

import com.arisys.createUser.service.UsersServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
// 모든 컨트롤러에서 발생한 예외를 처리할 수 있도록 설정
public class GlobalExceptionHandler {
    public static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserIsEmptyException.class)
    public ResponseEntity<String> handleUserIsEmptyException(UserIsEmptyException emptyEx){
        String message = emptyEx.getMessage();
        logger.warn("UserIsEmptyException occurred: {}", message);
        return new ResponseEntity(message, HttpStatus.NOT_FOUND);
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage)
        //메서드 체이닝을 사용하여 응답 상태 코드와 본문을 설정할 수도 있음
    }
    @ExceptionHandler(UserIdIsExistException.class)
    public ResponseEntity<String> handleUserIdIsExistException(UserIdIsExistException emptyEx){
        String message = emptyEx.getMessage();
        logger.warn("UserIdIsExistException occurred: Duplicate username", message);
        return new ResponseEntity(message, HttpStatus.CONFLICT);
    }
}
