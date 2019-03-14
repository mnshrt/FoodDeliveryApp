package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Karan Pillai (https://github.com/KaranP3)
 * Description - Controller for address related methods.
 */

@Controller
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;

    /**
     * Controller method for the Save Address endpoint
     *
     * @param access_token Access-token of the customer
     * @param saveAddressRequest SaveAddressRequest containing address details
     * @return ResponseEntity with SaveAddressResponse and HTTP status
     * @throws AuthorizationFailedException in cases where the access token is invalid
     * @throws SaveAddressException in cases where there the address details are invalid
     * @throws AddressNotFoundException in cases where the state UUID is invalid
     */
    @CrossOrigin
    @PostMapping(path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization")
                                                           final String access_token,
                                                           final SaveAddressRequest saveAddressRequest)
            throws AuthorizationFailedException, SaveAddressException,
            AddressNotFoundException {

        CustomerEntity customerEntity = customerService.getCustomer(access_token);

        // Create AddressEntity
        AddressEntity addressEntity = new AddressEntity();
        CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();

        StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());

        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setFlat_buil_number(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setStateEntity(stateEntity);
        addressEntity.setActive(1);

        if (!addressEntity.getPincode().matches("^[1-9][0-9]{5}$")){
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }


        String cityExists = String.valueOf(saveAddressRequest.getCity());
        String flatBuilNameExists = String.valueOf(saveAddressRequest.getFlatBuildingName());
        String localityExists = String.valueOf(saveAddressRequest.getLocality());
        String pincodeExists = String.valueOf(saveAddressRequest.getPincode());
        String stateUuidExists = String.valueOf(saveAddressRequest.getStateUuid());

        // If any of the fields except lastName are null or empty, throw exception
        if (cityExists.equals("null") || cityExists.isEmpty()
                || flatBuilNameExists.equals("null") || flatBuilNameExists.isEmpty()
                || localityExists.equals("null") || localityExists.isEmpty()
                || pincodeExists.equals("null") || pincodeExists.isEmpty()
                || stateUuidExists.equals("null")|| stateUuidExists.isEmpty()) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }

        customerAddressEntity.setCustomerEntity(customerEntity);
        customerAddressEntity.setAddressEntity(addressEntity);

        addressService.saveAddress(addressEntity, customerAddressEntity);

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                .id(addressEntity.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.OK);
    }

    /**
     * Controller method for the Get All Saved Addresses endpoint
     * 
     * @param access_token Access-token of the customer
     * @return ResponsEntity with AddressListResponse and HTTP Status
     * @throws AuthorizationFailedException in cases where the access token is invalid
     */
    @CrossOrigin
    @GetMapping(path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllSavedAddresses(@RequestHeader("authorization")
                                                                    final String access_token)
            throws AuthorizationFailedException{
        CustomerEntity customerEntity = customerService.getCustomer(access_token);

        List<AddressEntity> initialAddressList = addressService.getAllAddress(customerEntity);


        List<AddressList> addressList = new ArrayList<>();

        AddressListResponse addressListResponse = new AddressListResponse();

        if (initialAddressList.size() > 0){
            for (int i = 0; i < initialAddressList.size(); i++){
                AddressList addressListEl = new AddressList();
                String uuid = initialAddressList.get(i).getUuid();
                String stateUUID = initialAddressList.get(i).getStateEntity().getUuid();
                AddressListState addressListState = new AddressListState();

                addressListState.setId(UUID.fromString(initialAddressList.get(i).getStateEntity().getUuid()));

                addressListState.setStateName(initialAddressList.get(i).getStateEntity().getState_name());

                addressListEl.setId(UUID.fromString(uuid));
                addressListEl.setCity(initialAddressList.get(i).getCity());
                addressListEl.setFlatBuildingName(initialAddressList.get(i).getFlat_buil_number());
                addressListEl.setLocality(initialAddressList.get(i).getLocality());
                addressListEl.setPincode(initialAddressList.get(i).getPincode());
                addressListEl.setState(addressListState);
                addressListResponse.addAddressesItem(addressListEl);
            }
        } else {
            addressListResponse.setAddresses(Collections.emptyList());
        }

        return new  ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }
}
