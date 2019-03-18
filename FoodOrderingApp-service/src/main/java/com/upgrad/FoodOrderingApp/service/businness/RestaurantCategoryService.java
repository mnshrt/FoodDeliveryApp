package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RestaurantCategoryService {

    @Autowired
    RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    CategoryDao categoryDao;

    public List<String> getAllCategoriesByRestaurant(RestaurantEntity r) {
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getAllCategoriesByRestaurant(r);

        List<String> categories = new ArrayList<>();
        for (RestaurantCategoryEntity rcEntity : restaurantCategoryEntities) {
            categories.add(categoryDao.getCategoryName(rcEntity.getCategoryEntity()));
        }
        java.util.Collections.sort(categories, Collator.getInstance());
        return categories;

    }
}
