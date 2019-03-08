package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
     * Method to create a new customer.
     * @param customerEntity CustomerEntity to be create
     * @return created CustomerEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity createCustomer(final CustomerEntity customerEntity) {
        String password = customerEntity.getPassword();
        String[] encryptPassword = passwordCryptographyProvider.encrypt(password);
        String salt = encryptPassword[0];
        customerEntity.setSalt(salt);
        customerEntity.setPassword(encryptPassword[1]);
        return customerDao.createCustomer(customerEntity);
    }

    /**
     * Method to get customer by contact number
     * @param contactNumber Contact number of the user we are trying to find
     * @return CustomerEntity of given user, or null if the contact number is not found
     */
    public CustomerEntity getCustomerByContactNumber(String contactNumber){
        return customerDao.findUserByContactNumber(contactNumber);
    }

}
