package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - This class is the Service class for customer related methods.
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
        // Validate password format and length using regex
        if (!customerEntity.getPassword()
                .matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[A-Z])(?=.*[#@$%&*!^]).*$")) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
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

}
