package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.xml.ws.Response;

/**
 * Description - ExceptionHandler for all the exceptions to be implemented.
 */

@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Method that implements the exception handler for the SignUpRestrictedException.
     *
     * @author Karan Pillai (https://github.com/KaranP3)

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

    /**
     * Method that implements the exception handler for SaveAddressException
     *
     * @author Karan Pillai (https://github.com/KaranP3)
     *
     * @param ex instance of SaveAddressException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     */
    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> saveAddressException(SaveAddressException ex,
                                                              WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Method that implements the exception handler for AddressNotFoundException
     *
     * @author Karan Pillai (https://github.com/KaranP3)
     *
     * @param ex instance of AddressNotFoundException
     * @param request instance of WebRequest
     * @return ResponseEntity with the error response
     */
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> addressNotFoundException(AddressNotFoundException ex,
                                                                  WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> couponNotFoundException(CouponNotFoundException ex,
                                                                 WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.FORBIDDEN
        );
    }


    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> restaurantNotFoundException(RestaurantNotFoundException ex,
                                                                     WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> invalidRatingException(InvalidRatingException ex,
                                                                WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException ex,
                                                                   WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> itemNotFoundException(ItemNotFoundException ex,
                                                               WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getErrorMessage()),
                HttpStatus.NOT_FOUND
        );
    }


}
