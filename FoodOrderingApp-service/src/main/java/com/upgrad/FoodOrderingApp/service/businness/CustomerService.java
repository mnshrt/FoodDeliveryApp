package com.upgrad.FoodOrderingApp.service.businness;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - Service class for customer related methods.
 */

@Service
public class CustomerService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Method to create a new customer
     *
     * @param customerEntity CustomerEntity to be created
     * @return created CustomerEntity
     * @throws SignUpRestrictedException in cases where the CustomerEntity object fails validation checks
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {SignUpRestrictedException.class})
    public CustomerEntity saveCustomer(final CustomerEntity customerEntity) throws SignUpRestrictedException {
        // Get password, encrypt it, generate the salt and set them in the
        // instance of the CustomerEntity object
        String password = customerEntity.getPassword();
        String[] encryptPassword = passwordCryptographyProvider.encrypt(password);
        String salt = encryptPassword[0];
        customerEntity.setSalt(salt);
        customerEntity.setPassword(encryptPassword[1]);

        // Validate contact number format and length using regex
        if (!customerEntity.getContact_number().matches("^.*(?=.{10,})^[0-9]*$")) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }
        // Validate email format using regex
        if (!customerEntity.getEmail()
                .matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }

        // Once we have encrypted the password, generated the salt and validated the credentials,
        // create the customer in the DB and return customerEntity
        return customerDao.createCustomer(customerEntity);
    }

    /**
     * Method to get customer by contact number
     *
     * @param contactNumber Contact number of the user we are trying to find
     * @return CustomerEntity of given user, or null if the contact number is not found
     */
    public CustomerEntity getCustomerByContactNumber(String contactNumber) {
        // Query the DB and return associated CustomerEntity object
        return customerDao.findCustomerByContactNumber(contactNumber);
    }

    /**
     * Method to check customer credentials before updating
     * @param access_token Access token associated with the customer/session
     * @return true, if the credentials are successfully validated
     * @throws AuthorizationFailedException in cases where required
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean checkCustomer(String access_token) throws AuthorizationFailedException{
        CustomerAuthEntity customerAuthEntity = customerDao.findCustomerAuthEntityByAccessToken(access_token);
        if (customerAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (!String.valueOf(customerDao
                .findCustomerAuthEntityByAccessToken(access_token).getLogoutAt())
                .equals("null")){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint");
        } else if(customerAuthEntity.getExpiresAt() != null){
            ZonedDateTime expiryTime = customerAuthEntity.getExpiresAt();
            final ZonedDateTime currentTime = ZonedDateTime.now();
            if(expiryTime != null && expiryTime.isBefore(currentTime)) {
                throw new AuthorizationFailedException("ATHR-003",
                        "Your session is expired. Log in again to access this endpoint");
            }
        }
        return true;
    }

    /**
     * Method to get customer using access token
     * @param access_token Access token associated with the customer we are trying to find
     * @return CustomerEntity of the customer
     * @throws AuthorizationFailedException in cases where required
     */
    public CustomerEntity getCustomer(String access_token) throws AuthorizationFailedException{
       CustomerEntity customerEntity = null;
       boolean validify = checkCustomer(access_token);
       if(validify){
           customerEntity = customerDao.findCustomerAuthEntityByAccessToken(access_token).getCustomer();
    }
       return customerEntity;
    }

    /**
     * Method to get CustomerAuthEntity by access token
     * @param access_token Access token associated with the customer we are trying to find
     * @return CustomerAuthEntity of the customer
     * @throws AuthorizationFailedException in cases where required
     */
    public CustomerAuthEntity getCustomerAuth(String access_token) throws AuthorizationFailedException{
        CustomerAuthEntity customerAuthEntity = null;
        if (checkCustomer(access_token)){
            customerAuthEntity = customerDao.findCustomerAuthEntityByAccessToken(access_token);
        }
        return customerAuthEntity;
    }

    /**
     * Method to authenticate customer credentials
     *
     * @param email email to be used in authentication
     * @param password password to be used in authentication
     * @return customerAuthTokenEntity with the created access token assigned to the user
     * @throws AuthenticationFailedException in cases where the email doesn't exist, or the password is incorrect
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(final String email, final String password)
            throws AuthenticationFailedException {

        // Query the DB to check if the customer exists
        CustomerEntity customerEntity = customerDao.findCustomerByEmail(email);

        // If the customer does not exist, throw exception
        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        // Encrypt the password, and get salt
        final String encryptedPassword = PasswordCryptographyProvider
                .encrypt(password, customerEntity.getSalt());

        // If the passwords match, the authentication is complete
        // Else, throw exception due to invalid credentials
        if (encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider tokenProvider = new JwtTokenProvider(encryptedPassword);

            CustomerAuthEntity customerAuthTokenEntity = new CustomerAuthEntity();
            customerAuthTokenEntity.setCustomer(customerEntity);
            customerAuthTokenEntity.setUuid(customerEntity.getUuid());

            final ZonedDateTime currentTime = ZonedDateTime.now();
            final ZonedDateTime expiryTime = currentTime.plusHours(8);

            customerAuthTokenEntity.setAccessToken(tokenProvider
                    .generateToken(customerEntity.getUuid(), currentTime, expiryTime));
            customerAuthTokenEntity.setLoginAt(currentTime);
            customerAuthTokenEntity.setExpiresAt(expiryTime);

            customerDao.createAuthToken(customerAuthTokenEntity);
            return customerAuthTokenEntity;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }

    /**
     * Method to logout customer
     * @param access_token Access token associated with the session
     * @return CustomerAuthEntity of the customer
     * @throws AuthorizationFailedException in cases where required
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(String access_token) throws AuthorizationFailedException {

        final CustomerAuthEntity customerAuthEntity = customerDao
                .findCustomerAuthEntityByAccessToken(access_token);

        if (customerAuthEntity == null || customerAuthEntity.getUuid() == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        } else {
            final ZonedDateTime currentTime = ZonedDateTime.now();
            customerAuthEntity.setLogoutAt(currentTime);
            return customerAuthEntity;
        }
    }

    /**
     * Method to update customer details
     * @param customerEntity CustomerEntity of the customer
     * @return Updated CustomerEntity
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {UpdateCustomerException.class})
    public CustomerEntity updateCustomer(CustomerEntity customerEntity){
        customerDao.updateCustomerDetails(customerEntity);
        return customerEntity;
    }

    /**
     * Method to validate credentials for updating customer details
     * @param firstName First name of the customer
     * @return true if validated
     * @throws UpdateCustomerException in cases where firstName is an empty string or null
     */
    public boolean updateCredentialsValidifer(String firstName) throws UpdateCustomerException{
        if (firstName != null) {
            if (firstName.isEmpty() || firstName.matches("\".*\"")) {
                        throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
                    }
                } else if (firstName == null){
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
        return true;
    }

    /**
     * Method to validate input when updating password
     * @param oldPassword Old password of the given customer
     * @param newPassword New password of the given customer
     * @return true if the passwords are validated
     * @throws UpdateCustomerException in cases where the passwords are not validated
     */
    public boolean updatePasswordValidifier(String oldPassword, String newPassword) throws UpdateCustomerException{
        if (oldPassword != null && newPassword != null) {
            if (oldPassword.isEmpty() || oldPassword.matches("\".*\"") ||
                    newPassword.isEmpty() || newPassword.matches("\".*\"")) {
                throw new UpdateCustomerException("UCR-003", "No field should be empty");
            }
        } else if (oldPassword == null || newPassword == null){
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        return true;
    }

    /**
     * Method to update customer password
     * @param oldPassword Old password of the given customer
     * @param newPassword New password of the given customer
     * @param customerEntity CustomerEntity associated with the customer
     * @return CustomerEntity with new password and salt
     * @throws UpdateCustomerException in cases where required
     */
    public CustomerEntity updateCustomerPassword(String oldPassword,
                                                 String newPassword,
                                                 CustomerEntity customerEntity)
            throws UpdateCustomerException{
        // Extra layer of validation for the old and new password
        updatePasswordValidifier(oldPassword, newPassword);
        // Encrypt the password
        String[] encryptPassword = passwordCryptographyProvider.encrypt(newPassword);
        // Extract salt and assign to variable
        String salt = encryptPassword[0];
        // Set the salt
        customerEntity.setSalt(salt);
        // Set the new encrypted password
        customerEntity.setPassword(encryptPassword[1]);
        // Return customerEntity
        return customerEntity;
    }

    /**
     * Method to authenticate using the old password before updating customer password
     * @param password Old password of the given customer
     * @param customerEntity CustomerEntity associated with the customer
     * @return CustomerEntity associated with the customer
     * @throws UpdateCustomerException throw UpdateCustomerException when authenticating the old password fails
     */
    public boolean updatePasswordAuthentication(String password, CustomerEntity customerEntity) throws UpdateCustomerException{
        // If the customer does not exist, throw exception
        // Encrypt the password, and get salt
        final String encryptedPassword = PasswordCryptographyProvider
                .encrypt(password, customerEntity.getSalt());

        // If the passwords match, the authentication is complete
        // Else, throw exception due to invalid credentials
        if (encryptedPassword.equals(customerEntity.getPassword())) {
            return true;
        } else {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
    }

}
