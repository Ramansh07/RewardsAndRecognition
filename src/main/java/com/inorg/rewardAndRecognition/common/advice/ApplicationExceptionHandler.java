package com.inorg.rewardAndRecognition.common.advice;

import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.ErrorDetail;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.common.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleInvalidRequestAtDTO(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetail("400", error.getField() + ": " + error.getDefaultMessage()))
                .collect(Collectors.toList());

        ResponseDTO response = ResponseDTO.builder()
                .success(false)
                .message("Validation errors occurred")
                .completionTimeStamp(LocalDateTime.now())
                .errorDetails(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseDTO response = ResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .completionTimeStamp(LocalDateTime.now())
                .errorDetails(List.of(new ErrorDetail("404", ex.getMessage())))
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequest.class)
    public ResponseEntity<ResponseDTO> handleInvalidRequest(InvalidRequest ex) {
        ResponseDTO response = ResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .completionTimeStamp(LocalDateTime.now())
                .errorDetails(List.of(new ErrorDetail("400", ex.getMessage())))
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAuthorisationException.class)
    public ResponseEntity<ResponseDTO> handleNoAuthorisationException(NoAuthorisationException ex) {
        ResponseDTO response = ResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .completionTimeStamp(LocalDateTime.now())
                .errorDetails(List.of(new ErrorDetail("403", ex.getMessage())))
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGlobalException(Exception ex) {
        ResponseDTO response = ResponseDTO.builder()
                .success(false)
                .message("Internal Server Error")
                .completionTimeStamp(LocalDateTime.now())
                .errorDetails(List.of(new ErrorDetail("500", ex.getMessage())))
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
