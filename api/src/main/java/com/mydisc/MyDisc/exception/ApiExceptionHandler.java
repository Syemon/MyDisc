package com.mydisc.MyDisc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(FolderNotFoundException exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(FileNotFoundException exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
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

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(MethodArgumentTypeMismatchException exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Parameter " + "\"" + exc.getName() + "\" is incorrect");
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(RequiredParameterException exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(HttpMessageNotReadableException exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Data is required");
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(NotEnoughDiscSpaceException exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(Exception exc) {
        ApiErrorResponse error = new ApiErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage() + exc.toString());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}