package com.upgrad.FoodOrderingApp.service.businness;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * Method to get all addresses associated with a given customer
     * @param customerEntity CustomerEntity of the customer
     * @return List containing AddressEntity of the addresses associated with the customer
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
}
