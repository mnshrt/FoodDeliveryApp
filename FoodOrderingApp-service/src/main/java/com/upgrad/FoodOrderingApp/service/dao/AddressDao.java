package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.plaf.nimbus.State;
import java.util.List;

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

    public AddressEntity findAddressByUUID(String uuid){
        try {
            String query = "select u from AddressEntity u where u.uuid = :uuid";
            return entityManager.createQuery(query, AddressEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method to find all addresses associated with a given customer
     * @param id Id of the customer
     * @return List of CustomerAddressEntity's associated with the customer
     */
    public List<CustomerAddressEntity> findAllSavedAddresses(final int id){
        try{
            String query = "select u from CustomerAddressEntity u where u.customerEntity.id = :id order by u.addressEntity.id desc";
            return entityManager.createQuery(query, CustomerAddressEntity.class)
                    .setParameter("id", id).getResultList();
        } catch(NoResultException nre){
            return null;
        }
    }

    /**
     * Method to find all states
     * @return List of all the StateEntity's available in the DB.
     */
    public List<StateEntity> findAllStates(){
        try{
            String query = "select u from StateEntity u";
            return entityManager.createQuery(query, StateEntity.class)
                    .getResultList();
        } catch(NoResultException nre) {
            return null;
        }
    }

    /**
     * Method to delete an address by UUID
     * @param uuid UUID of the address to be deleted
     */
    public void deleteAddressByUUID(final String uuid){
        String query = "delete from AddressEntity u where u.uuid = :uuid";
        Query finalQuery = entityManager.createQuery(query).setParameter("uuid", uuid);
        finalQuery.executeUpdate();
    }

    public AddressEntity getAddressById(AddressEntity address) {

        try {
            String query = "select u from AddressEntity u where u.id = :userInput";
            return entityManager.createQuery(query, AddressEntity.class)
                    .setParameter("userInput", address.getId()).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public StateEntity getStateById(StateEntity state) {


        try {
            String query = "select u from StateEntity u where u.id= :userInput";
            return entityManager.createQuery(query, StateEntity.class)
                    .setParameter("userInput", state.getId()).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
