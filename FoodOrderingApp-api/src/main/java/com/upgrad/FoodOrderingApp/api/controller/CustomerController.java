package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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

    @CrossOrigin
    @PostMapping(path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> signUp(
            @RequestBody(required = false) SignupCustomerRequest signupCustomerRequest)
            throws SignUpRestrictedException {

        // Create customer entity
        final CustomerEntity customerEntity = new CustomerEntity();

        // Set customer details by getting values from signUpCustomerRequest
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstname(signupCustomerRequest.getFirstName());
        customerEntity.setLastname(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setContact_number(signupCustomerRequest.getContactNumber());
        customerEntity.setPassword(signupCustomerRequest.getPassword());

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
            final CustomerEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);
            SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse()
                    .id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
            return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
        }
    }

    @CrossOrigin
    @PostMapping(path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization")
                                                       String authorization)
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
                .firstName(customerEntity.getFirstname())
                .lastName(customerEntity.getLastname())
                .contactNumber(customerEntity.getContact_number())
                .emailAddress(customerEntity.getEmail())
                .message("LOGGED IN SUCCESSFULLY");

        // Add access token to the header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-token", customerAuthToken.getAccessToken());

        // Return loginResponse, header, and the corresponding HTTP status
        return new ResponseEntity<LoginResponse>(loginResponse, httpHeaders, HttpStatus.OK);
    }
}
