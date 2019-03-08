package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

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
        customerEntity.setFirstname(signupCustomerRequest.getFirstName());
        customerEntity.setLastname(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setContact_number(signupCustomerRequest.getContactNumber());
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        // Validate email format using regular expressions
        if (!signupCustomerRequest.getEmailAddress()
                .matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")){
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }

        // Validate contact number format and length using regular expressions
        if (!signupCustomerRequest.getContactNumber().matches("^.*(?=.{10,})^[0-9]*$")){
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }

        // Validate password format and length using regular expressions
        if (!signupCustomerRequest.getPassword()
                .matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[A-Z])(?=.*[#@$%&*!^]).*$")){
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        // Variables to perform further validation
        String customerExists = String.valueOf(customerService.getCustomerByContactNumber(signupCustomerRequest.getContactNumber()));
        String contactNumberExists = String.valueOf(signupCustomerRequest.getContactNumber());
        String firstNameExists = String.valueOf(signupCustomerRequest.getFirstName());
        String emailExists = String.valueOf(signupCustomerRequest.getEmailAddress());
        String passwordExists = String.valueOf(signupCustomerRequest.getPassword());

        // If any of the fields are null or empty, throw exception
        // Else if a customer with the same contact number already exists in the DB, throw exception
        // Else create new customer and return signUpCustomerResponse and corresponding HTTP status
        if (contactNumberExists.equals("null") || contactNumberExists.isEmpty()
                || firstNameExists.equals("null") || firstNameExists.isEmpty()
                || emailExists.equals("null") || emailExists.isEmpty()
                || passwordExists.equals("null") || passwordExists.isEmpty()) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        } else if (!customerExists.equals("null")) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        } else {
            final CustomerEntity createdCustomerEntity = customerService.createCustomer(customerEntity);
            SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse()
                    .id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
            return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
        }
    }
}
