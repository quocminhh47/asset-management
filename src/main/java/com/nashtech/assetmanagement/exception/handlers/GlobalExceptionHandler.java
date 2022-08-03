package com.nashtech.assetmanagement.exception.handlers;

import com.nashtech.assetmanagement.dto.response.ErrorResponse;
import com.nashtech.assetmanagement.dto.response.ResponseErrorMessage;
import com.nashtech.assetmanagement.exception.DateInvalidException;
import com.nashtech.assetmanagement.exception.NotUniqueException;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.exception.UnauthorizedException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse("404", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnwantedException(Exception ex) {
        ResponseErrorMessage errorResponse = new ResponseErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), new Date());
        return new ResponseEntity<>(errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UnauthorizedException.class})
    protected ResponseEntity<ErrorResponse> handleUnauthorizedException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse("401", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({DateInvalidException.class, IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<ErrorResponse> handleDateInvalidException(RuntimeException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        Map<String, List<String>> body = new HashMap<>();
//
//        List<String> errors = ex.getBindingResult()
//                .getAllErrors()
//                .stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
//        body.put("errors", errors);
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
    @ExceptionHandler({NotUniqueException.class})
    protected ResponseEntity<ResponseErrorMessage> handleNotUniqueException(RuntimeException exception) {
        ResponseErrorMessage responseErrorMessage =
                new ResponseErrorMessage(HttpStatus.CONFLICT,exception.getMessage(),
                        new Date());
        return new ResponseEntity<>(responseErrorMessage, responseErrorMessage.getStatus());
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        String message="";
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
            message=error.getDefaultMessage();
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ResponseErrorMessage apiError =
                new ResponseErrorMessage(HttpStatus.BAD_REQUEST,
                        message, errors,new Date());
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }


}
