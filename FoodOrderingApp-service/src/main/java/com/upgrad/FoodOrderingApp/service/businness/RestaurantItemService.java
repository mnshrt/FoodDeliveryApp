package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.RestaurantItemDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantItemService {

    @Autowired
    RestaurantItemDao restaurantItemDao;

    public List<RestaurantItemEntity> getItemsByRestaurant(RestaurantEntity re) {
        return restaurantItemDao.getItemsByRestaurant(re);
    }
}
