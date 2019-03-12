package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
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
     * Method that implements the exception handler for the SignUpRestrictedException.
     *
     * @author Karan Pillai (https://github.com/KaranP3)
     *
     * @param ex      instance of SignUpRestrictedException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     */

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException ex,
                                                                   WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Method that implements the exception handler for AuthenticationFailedException
     *
     * @author Karan Pillai (https://github.com/KaranP3)
     *
     * @param ex      instance of AuthenticationFailedException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     */

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException ex,
                                                                       WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    /**
     * Method that implements the exception handler for AuthorizationFailedException
     *
     * @author Karan Pillai (https://github.com/KaranP3)
     *
     * @param ex instance of AuthorizationFailedException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException ex,
                                                                      WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.FORBIDDEN);
    }

    /**
     * Method that implements the exception handler for UpdateCustomerException
     *
     * @author Karan Pillai (https://github.com/KaranP3)
     *
     * @param ex instance of UpdateCustomerException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     */
    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updateCustomerException(UpdateCustomerException ex,
                                                                 WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
