package com.nashtech.assetmanagement.exception;

import com.nashtech.assetmanagement.dto.response.ResponseErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex) {
        ResponseErrorMessage errorResponse =
                new ResponseErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage(),
                        ex.getMessage(), new Date());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnwantedException(Exception ex) {
        ResponseErrorMessage errorResponse = new ResponseErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), new Date());
        return new ResponseEntity<>(errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
