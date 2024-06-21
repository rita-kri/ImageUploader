package com.imageupload.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.imageupload.helpers.CustomResponse;

@ControllerAdvice
public class GlobalExceptionhandler {

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<CustomResponse> handleImageUploadException(ImageUploadException imageUploadException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomResponse.builder()
            .message(imageUploadException.getMessage())
            .success(false)
            .build());
    }

}
