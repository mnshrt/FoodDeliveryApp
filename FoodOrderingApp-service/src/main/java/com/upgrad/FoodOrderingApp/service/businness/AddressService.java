package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    @Autowired
    AddressDao addressDao;

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
}
