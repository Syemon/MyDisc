package com.mydisc.MyDisc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(Exception exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage() + "ake lipa:(( +" +exc.toString());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(NotFoundFolderException exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage() + "ake lipa:(((");
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(MethodArgumentNotValidException exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        BindingResult result = exc.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(fieldErrors.toString());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}