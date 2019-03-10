package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Description - ExceptionHandler for all the exceptions to be implemented.
 */

@ControllerAdvice
public class RestExceptionHandler {

    /**
     * @param ex      instance of SignUpRestrictedException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     * @author Karan Pillai (https://github.com/KaranP3)
     * <p>
     * Method that implements the exception handler for the SignUpRestrictedException.
     */

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException ex,
                                                                   WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * @param ex      instance of AuthenticationFailedException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     * @author Karan Pillai (https://github.com/KaranP3)
     * <p>
     * Method that implements the exception handler for AuthenticationFailedException
     */

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException ex,
                                                                       WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }
}
