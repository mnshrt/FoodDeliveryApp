package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException ex,
                                                                   WebRequest request){
        if (ex.getCode().equals("SGR-001")){
            return new ResponseEntity<ErrorResponse>(new ErrorResponse()
            .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                    .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
