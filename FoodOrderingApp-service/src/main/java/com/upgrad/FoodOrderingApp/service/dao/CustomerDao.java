package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - DAO class with operations for the customer table
 */

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to create customer in the DB
     *
     * @param customerEntity CustomerEntity with details of the customer to be created
     * @return CustomerEntity of the created customer
     */
    public CustomerEntity createCustomer(final CustomerEntity customerEntity) {
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    /**
     * Method to create auth token in DB.
     *
     * @param customerAuthTokenEntity customerAuthTokenEntity with the token to be created
     * @return UserAuthTokenEntity of the created auth token
     */
    public CustomerAuthEntity createAuthToken(final CustomerAuthEntity customerAuthTokenEntity) {
        entityManager.persist(customerAuthTokenEntity);
        return customerAuthTokenEntity;
    }

    /**
     * Method to find customer by contact number
     *
     * @param contact_number Contact number of the customer to be found
     * @return If the customer is found, return CustomerEntity of the given user, else return null
     */
    public CustomerEntity findCustomerByContactNumber(final String contact_number) {
        try {
            String query = "select u from CustomerEntity u where u.contact_number = :contact_number";
            return entityManager.createQuery(query, CustomerEntity.class)
                    .setParameter("contact_number", contact_number).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method to find customer by email in the DB.
     *
     * @param email Email id of the customer to be found
     * @return If the customer is found, return CustomerEntity of the given customer, else return null
     */
    public CustomerEntity findCustomerByEmail(final String email) {
        try {
            String query = "select u from CustomerEntity u where u.email = :email";
            return entityManager.createQuery(query, CustomerEntity.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method to get CustomerAuthEntity from the db given an access token.
     * @param access_token String containing access token of customer
     * @return CustomerAuthEntity containing the given access token
     */
    public CustomerAuthEntity findCustomerAuthEntityByAccessToken(final String access_token){
        try{
            String query = "select u from CustomerAuthEntity u where u.accessToken = :accessToken";
            return entityManager.createQuery(query, CustomerAuthEntity.class)
                    .setParameter("accessToken", access_token).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerEntity updateCustomerDetails(final CustomerEntity customerEntity){
        entityManager.merge(customerEntity);
        return customerEntity;
    }
}
