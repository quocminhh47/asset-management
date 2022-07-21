package com.nashtech.assetmanagement.dto.response;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ResponseErrorMessage {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ResponseErrorMessage(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
    public ResponseErrorMessage(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors= Arrays.asList(error);
    }

}
