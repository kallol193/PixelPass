package com.BekaarCompany.PixelPass.exceptions;

import com.BekaarCompany.PixelPass.enums.Messages;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Configuration
public class GlobalExceptionHandler {

    @ExceptionHandler(NoContentExistException.class)
    public ResponseEntity<?> HandleNoContentExistException(){
        return new ResponseEntity<>(Messages.NO_CONTENT_EXIST.toString(), HttpStatus.NO_CONTENT);
    }

}
