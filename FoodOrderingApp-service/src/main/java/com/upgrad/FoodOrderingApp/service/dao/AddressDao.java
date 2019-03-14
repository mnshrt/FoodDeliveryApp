package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - DAO class with operations for the address table
 */

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to save an address in the DB.
     * @param addressEntity AddressEntity with the details of the address to be saved
     * @return AddressEntity of the saved address
     */
    public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerAddressEntity customerAddressEntity){
        entityManager.persist(customerAddressEntity);
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    /**
     * Method to find a state by the state UUID
     * @param uuid UUID of the given state
     * @return return StateEntity if the state is found, else return null
     */
    public StateEntity findStateByUUID(final String uuid){
        try {
            String query = "select u from StateEntity u where u.uuid = :uuid";
            return entityManager.createQuery(query, StateEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
