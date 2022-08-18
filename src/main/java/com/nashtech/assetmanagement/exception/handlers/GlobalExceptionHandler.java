package com.nashtech.assetmanagement.exception.handlers;

import com.nashtech.assetmanagement.dto.response.ErrorResponseDto;
import com.nashtech.assetmanagement.dto.response.ErrorResponseMessageDto;
import com.nashtech.assetmanagement.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(RuntimeException exception) {
        ErrorResponseDto error = new ErrorResponseDto("404", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnwantedException(Exception ex) {
        ErrorResponseMessageDto errorResponse = new ErrorResponseMessageDto(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), new Date());
        return new ResponseEntity<>(errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UnauthorizedException.class})
    protected ResponseEntity<ErrorResponseDto> handleUnauthorizedException(RuntimeException exception) {
        ErrorResponseDto error = new ErrorResponseDto("401", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<ErrorResponseDto> handleBadRequestException(RuntimeException exception) {
        ErrorResponseDto error = new ErrorResponseDto(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({DateInvalidException.class, IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<ErrorResponseDto> handleDateInvalidException(RuntimeException exception) {
        ErrorResponseDto error = new ErrorResponseDto(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({NotUniqueException.class})
    protected ResponseEntity<ErrorResponseMessageDto> handleNotUniqueException(RuntimeException exception) {
        ErrorResponseMessageDto responseErrorMessage =
                new ErrorResponseMessageDto(HttpStatus.BAD_REQUEST, exception.getMessage(),
                        new Date());
        return new ResponseEntity<>(responseErrorMessage, responseErrorMessage.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<>();
        String message = "";
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
            message = error.getDefaultMessage();
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorResponseMessageDto apiError =
                new ErrorResponseMessageDto(HttpStatus.BAD_REQUEST,
                        message, errors, new Date());
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponseMessageDto> handleBadCredentialsException(RuntimeException exception) {
        ErrorResponseMessageDto responseErrorMessage =
                new ErrorResponseMessageDto(HttpStatus.UNAUTHORIZED, exception.getMessage(),
                        new Date());
        return new ResponseEntity<>(responseErrorMessage, responseErrorMessage.getStatus());
    }
    @ExceptionHandler(RequestNotAcceptException.class)
    protected ResponseEntity<ErrorResponseMessageDto> handleRequestNotAcceptException(RuntimeException exception) {
        ErrorResponseMessageDto responseErrorMessage =
                new ErrorResponseMessageDto(HttpStatus.NOT_ACCEPTABLE, exception.getMessage(),
                        new Date());
        return new ResponseEntity<>(responseErrorMessage, responseErrorMessage.getStatus());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponseMessageDto> handleDataIntegrityViolationException(RuntimeException exception) {
        ErrorResponseMessageDto responseErrorMessage =
                new ErrorResponseMessageDto(HttpStatus.NOT_ACCEPTABLE, "Value too long " +
                        "for type character max(200)",
                        new Date());
        return new ResponseEntity<>(responseErrorMessage, responseErrorMessage.getStatus());
    }
}
