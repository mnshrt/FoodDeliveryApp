package com.upgrad.FoodOrderingApp.service.businness;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - Service class for address related methods.
 */

@Service
public class AddressService {

    @Autowired
    AddressDao addressDao;

    /**
     * Method to save address
     * @param addressEntity AddressEntity to be created
     * @param customerAddressEntity  CustomerEntity of the associated customer
     * @return created AddressEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerAddressEntity customerAddressEntity){
        addressDao.saveAddress(addressEntity, customerAddressEntity);
        return addressEntity;
    }

    /**
     * Method to get state by UUID
     *
     * @param uuid UUID of the state
     * @return StateEntity of the state associated with the UUID
     * @throws AddressNotFoundException in cases where a state corresponding to the given UUID is
     * not found
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateByUUID(final String uuid) throws AddressNotFoundException {
        StateEntity stateEntity = addressDao.findStateByUUID(uuid);
        if (stateEntity == null){
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        } else {
            return stateEntity;
        }
    }

    /**
     * Method to get address by UUID
     * @param uuid UUID of the address
     * @param customerEntity CustomerEntity of the customer
     * @return AddressEntity corresponding with the UUID param
     * @throws AddressNotFoundException in cases where the address id is empty or the address is not found in the DB
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressByUUID(final String uuid, final CustomerEntity customerEntity) throws
            AddressNotFoundException, AuthorizationFailedException{
        String uuidExists = String.valueOf(uuid);
        if (uuidExists.equals("null") || uuid.isEmpty()){
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }

        AddressEntity addressEntity = addressDao.findAddressByUUID(uuid);
        if (addressEntity == null){
            throw new AddressNotFoundException("ANF 003", "No address by this id");
        }

        List<AddressEntity> addressEntityList = getAllAddress(customerEntity);
        if (!addressEntityList.contains(addressEntity)){
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address ");
        } else {
            return addressEntity;
        }
    }

    /**
     * Method to delete address
     * @param uuid UUID of the address to be deleted
     * @return UUID of the address that has been deleted
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UUID deleteAddress(final String uuid){
        addressDao.deleteAddressByUUID(uuid);
        return UUID.fromString(uuid);
    }

    /**
     * Method to get all addresses associated with a given customer
     * @param customerEntity CustomerEntity of the customer
     * @return List containing AddressEntity of the customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity){
        List<CustomerAddressEntity> addressList = new ArrayList<>();
        addressList = addressDao.findAllSavedAddresses(customerEntity.getId());
        List<AddressEntity> finalAddressList = new ArrayList<>();
        if (addressList == null) {
            return Collections.emptyList();
        } else {
            for (int i = 0; i < addressList.size(); i++){
                AddressEntity addressEntity = addressList.get(i).getAddressEntity();
                finalAddressList.add(i, addressEntity);
            }
        }
        return finalAddressList;
    }

    /**
     * Method to get all states
     * @return List containing all StateEntity's in the DB
     */
    public List<StateEntity> getAllStates(){
        List<StateEntity> stateEntityList = addressDao.findAllStates();
        if (stateEntityList == null){
            return Collections.emptyList();
        } else {
            return stateEntityList;
        }
    }
}
