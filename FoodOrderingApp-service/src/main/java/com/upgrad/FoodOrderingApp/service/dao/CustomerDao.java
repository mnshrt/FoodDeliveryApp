package com.upgrad.FoodOrderingApp.service.dao;

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
     * @param customerEntity CustomerEntity with details of the customer to be created
     * @return CustomerEntity of the created customer
     */
    public CustomerEntity createCustomer(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    public CustomerEntity findUserByContactNumber(String contact_number){
        try{

            String query = "select u from CustomerEntity u where u.contact_number = :contact_number";
            return entityManager.createQuery(query, CustomerEntity.class)
                    .setParameter("contact_number", contact_number).getSingleResult();
        } catch(NoResultException nre){
            return null;
        }
    }
}
