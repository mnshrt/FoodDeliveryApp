package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - Controller for customer related methods.
 */

@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    /**
     * Controller method for the signup endpoint
     *
     * @param signupCustomerRequest SignUpCustomerRequest containing customer details
     * @return ResponseEntity containing the SignUpCUstomerRequest and HTTP Status
     * @throws SignUpRestrictedException throw SignUpRestrictedException in required cases
     */
    @CrossOrigin
    @PostMapping(path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> signUp(
            @RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest)
            throws SignUpRestrictedException {

        // Create customer entity
        final CustomerEntity customerEntity = new CustomerEntity();

        // Set customer details by getting values from signUpCustomerRequest
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setContact_number(signupCustomerRequest.getContactNumber());
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        // Validate password format and length using regex
        if (!customerEntity.getPassword()
                .matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[A-Z])(?=.*[#@$%&*!^]).*$")) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        // Variables to perform validation
        String customerExists = String.valueOf(customerService.getCustomerByContactNumber(signupCustomerRequest.getContactNumber()));
        String contactNumberExists = String.valueOf(signupCustomerRequest.getContactNumber());
        String firstNameExists = String.valueOf(signupCustomerRequest.getFirstName());
        String emailExists = String.valueOf(signupCustomerRequest.getEmailAddress());
        String passwordExists = String.valueOf(signupCustomerRequest.getPassword());

        // If any of the fields except lastName are null or empty, throw exception
        if (contactNumberExists.equals("null") || contactNumberExists.isEmpty()
                || firstNameExists.equals("null") || firstNameExists.isEmpty()
                || emailExists.equals("null") || emailExists.isEmpty()
                || passwordExists.equals("null") || passwordExists.isEmpty()) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }

        // If any of the fields are null or empty, throw exception
        // Else if a customer with the same contact number already exists in the DB, throw exception
        // Else create new customer and return signUpCustomerResponse and corresponding HTTP status
        if (!customerExists.equals("null")) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        } else {
            CustomerEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);
            SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse()
                    .id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
            return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
        }
    }

    /**
     * Controller method for login endpoint
     *
     * @param authorization Authorization header for Basic authentication.
     *                      Format - "Basic email:password"
     *                      Note: email:password needs to be base64 encoded
     * @return ResponseEntity with login response, header containing access token and HTTP status
     * @throws AuthenticationFailedException throw AuthenticationFailedException in required cases
     */
    @CrossOrigin
    @PostMapping(path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization)
            throws AuthenticationFailedException {

        // Initial validation for basic authentication

        // Split authorization header and validate base64 encoding using regex
        String splitAuthHeader = authorization.split("Basic ")[1];
        if (!splitAuthHeader
                .matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")) {
            throw new AuthenticationFailedException("ATH-003",
                    "Incorrect format of decoded customer name and password");
        }

        // Validate basic authentication formatting in the authorization header
        if (!authorization.startsWith("Basic")) {
            throw new AuthenticationFailedException("ATH-003",
                    "Incorrect format of decoded customer name and password");
        }

        // Decode authorization header after initial validation
        byte[] decodeAuth = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedAuth = new String(decodeAuth);
        String[] decodedAuthArray = decodedAuth.split(":");

        // Create instance of CustomerAuthEntity
        CustomerAuthEntity customerAuthToken = new CustomerAuthEntity();

        // Final basic authentication validation check
        if (decodedAuthArray.length > 0) {
            customerAuthToken = customerService
                    .authenticate(decodedAuthArray[0], decodedAuthArray[1]);
        } else {
            throw new AuthenticationFailedException("ATH-003",
                    "Incorrect format of decoded customer name and password");
        }

        // Get the associated CustomerEntity
        CustomerEntity customerEntity = customerAuthToken.getCustomer();

        // Build LoginResponse
        LoginResponse loginResponse = new LoginResponse()
                .id(customerEntity.getUuid())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .contactNumber(customerEntity.getContact_number())
                .emailAddress(customerEntity.getEmail())
                .message("LOGGED IN SUCCESSFULLY");

        // Add access token to the header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-token", customerAuthToken.getAccessToken());

        // Return loginResponse, header, and the corresponding HTTP status
        return new ResponseEntity<LoginResponse>(loginResponse, httpHeaders, HttpStatus.OK);
    }

    /**
     * Controler method for logout endpoint
     *
     * @param authorization Authorization header. Format - "Bearer access-token"
     * @return ResponseEntity with logout response and HTTP status
     * @throws AuthorizationFailedException throw AuthorizationFailedException in required cases
     */
    @CrossOrigin
    @PostMapping(path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logOut(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {
        // Split authorization header and get the access token
        String accessToken = authorization.split("Bearer ")[1];
        // Get the associated CustomerAuthEntity
        CustomerAuthEntity customerAuthEntity = customerService.getCustomerAuth(accessToken);

        // If the the customerAuthEntity is not null and the getLogOutAt field is not null,
        // throw an error
        // Else, proceed
        if (customerAuthEntity != null) {
            if (customerAuthEntity.getLogoutAt() != null) {
                throw new AuthorizationFailedException("ATH-002",
                        "Customer is logged out. Log in again to access this endpoint");
            }

            // Get expiry time of the associated access token
            ZonedDateTime expiryTime = customerAuthEntity.getExpiresAt();
            // Get current time
            final ZonedDateTime currentTime = ZonedDateTime.now();

            // If the expiry time is not null and is before the current time, throw an error
            // Else, proceed
            if (expiryTime != null) {
                if (expiryTime.isBefore(currentTime)) {
                    throw new AuthorizationFailedException("ATHR-003",
                            "Your session is expired. Log in again to access this endpoint");
                }
            }
        }

        // Create the final CustomerAuthEntity instance, and logout the customer
        CustomerAuthEntity finalCustomerAuthEntity = customerService.logout(accessToken);
        // Create logout response
        final LogoutResponse logoutResponse = new LogoutResponse()
                .id(finalCustomerAuthEntity.getCustomer().getUuid())
                .message("LOGGED OUT SUCCESSFULLY");
        // Return response entity with logout response and HTTP status
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }

    /**
     * Controller method for update endpoint
     *
     * @param access_token Access-token of the customer
     * @param updateCustomerRequest UpdateCustomerRequest containing the details that need to be updated
     * @return ResponseEntity with update customer response and HTTP status
     * @throws AuthorizationFailedException throw AuthorizationFailedException in required cases
     * @throws UpdateCustomerException      throw UpdateCustomerException in required cases
     */
    @CrossOrigin
    @PutMapping(path = "/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> update(@RequestHeader("authorization") final String access_token,
                                                         UpdateCustomerRequest updateCustomerRequest)
            throws AuthorizationFailedException, UpdateCustomerException {
        // Validate the input
        boolean validateInput = customerService.updateCredentialsValidifer(updateCustomerRequest.getFirstName());
        // Perform authorization validation logic and get the associated CustomerEntity
        CustomerEntity customerEntity = customerService.getCustomer(access_token);

        // Initialize uuid to am empty String
        String uuid = "";
        // Initialize firstName to an empty String
        String firstName = "";
        // Initialize lastName to an empty String
        String lastName = "";

        // If the customerEntity is not null, proceed
        // Else, throw exception
        if (customerEntity != null) {
            // If the input is validated, update the firstName and lastName fields
            // in customerEntity and set the same for the variables firstName and
            // lastName
            // Additionally, use the customerEntity.getUuid() method and assign the
            // result to the uuid variable
            if (validateInput) {
                customerEntity.setFirstName(updateCustomerRequest.getFirstName());
                firstName = updateCustomerRequest.getFirstName();
                uuid = customerEntity.getUuid();
                customerEntity.setLastName(updateCustomerRequest.getLastName());
                lastName = updateCustomerRequest.getLastName();
            }
        } else {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not logged in");
        }

        // Update the customer details
        customerService.updateCustomer(customerEntity);

        // Create update customer response
        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse()
                .id(uuid)
                .firstName(firstName)
                .lastName(lastName)
                .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");

        // Return response entity with update customer response and HTTP status
        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
    }

    /**
     * Controller method for change password endpoint
     *
     * @param access_token Access-token of the customer
     * @param updatePasswordRequest UpdatePasswordRequest containing the old and new passwords
     * @return UpdatePasswordResponse with UUID and HTTP status
     * @throws UpdateCustomerException in cases where required
     * @throws AuthorizationFailedException in cases where required
     */
    @CrossOrigin
    @PutMapping(path = "/customer/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestHeader("authorization") final String access_token,
                                                                 UpdatePasswordRequest updatePasswordRequest)
            throws UpdateCustomerException, AuthorizationFailedException{
        // Validate the input
        boolean validateInput = customerService.updatePasswordValidifier(
                updatePasswordRequest.getOldPassword(),
                updatePasswordRequest.getNewPassword());
        // Perform authorization validation logic and get the associated CustomerEntity
        CustomerEntity customerEntity = customerService.getCustomer(access_token);

        boolean updateAuth = customerService.updatePasswordAuthentication(updatePasswordRequest.getOldPassword(), customerEntity);

        if(customerEntity != null){
            if (!updatePasswordRequest.getNewPassword()
                    .matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[A-Z])(?=.*[#@$%&*!^]).*$")) {
                throw new UpdateCustomerException("UCR-001", "Weak password!");
            }
        }

        // Initialize uuid to am empty String
        String uuid = "";

        if (customerEntity != null && updateAuth) {
            // If the input is validated, update the firstName and lastName fields
            // in customerEntity and set the same for the variables firstName and
            // lastName
            // Additionally, use the customerEntity.getUuid() method and assign the
            // result to the uuid variable
            if (validateInput) {
               customerService.updateCustomerPassword(
                        updatePasswordRequest.getNewPassword(),
                       updatePasswordRequest.getNewPassword(),
                       customerEntity);
                uuid = customerEntity.getUuid();
            }
        } else {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not logged in");
        }

        // Update the customer password
        customerService.updateCustomer(customerEntity);

        // Create update password response
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse()
                .id(uuid)
                .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");

        // Return response entity with update password response and HTTP status
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
    }
}
