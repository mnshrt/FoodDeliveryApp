package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantDao restaurantDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getAllRestaurants() {

        return restaurantDao.getAllRestaurants();


    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getAllRestaurantsByName(String name) {

        return restaurantDao.getAllRestaurantsByName(name);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity getRestaurantById(String rUuid) throws RestaurantNotFoundException {
        RestaurantEntity re = restaurantDao.getRestaurantById(rUuid);
        if (re == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        } else {
            return re;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity getRestaurantByRestaurantId(RestaurantEntity re) throws RestaurantNotFoundException {
        RestaurantEntity r = restaurantDao.getRestaurantByRestaurantId(re);
        if (r == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        } else {
            return re;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantDetails(RestaurantEntity re) {
        return restaurantDao.updateRestaurantDetails(re);
    }
}