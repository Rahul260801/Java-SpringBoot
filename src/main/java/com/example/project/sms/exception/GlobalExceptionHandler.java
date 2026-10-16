package com.example.project.sms.exception;

import com.example.project.sms.payload.ApiResponse;
import com.example.project.sms.payload.ErrorResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity <ApiResponse<Object>> handleStudentNotFoundException(StudentNotFoundException ex, HttpServletRequest request) {
        ApiResponse<Object> response = new  ApiResponse<>(
                false,
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity <ApiResponse<Object>> handleDuplicateEmail(DuplicateEmailException ex, HttpServletRequest request) {
        ApiResponse<Object> response = new  ApiResponse<>(
                false,
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity <ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest req){
        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        ApiResponse<Object> response = new  ApiResponse<>(
                false,
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation fail",
                errors
        );
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
