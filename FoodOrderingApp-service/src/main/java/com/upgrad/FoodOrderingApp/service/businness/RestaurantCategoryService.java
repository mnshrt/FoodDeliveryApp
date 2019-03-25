package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> getAllCategoriesByRestaurant(RestaurantEntity r) {
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getAllCategoriesByRestaurant(r);
        //   System.out.println(restaurantCategoryEntities);
        List<String> categories = new ArrayList<>();
        for (RestaurantCategoryEntity rcEntity : restaurantCategoryEntities) {
            categories.add(categoryDao.getCategoryName(rcEntity.getCategoryEntity()));
        }
        java.util.Collections.sort(categories, Collator.getInstance());
        return categories;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getAllCategoryEntitiesByRestaurant(RestaurantEntity r) {
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getAllCategoriesByRestaurant(r);
        //  System.out.println(restaurantCategoryEntities);
        List<CategoryEntity> categories = new ArrayList<>();
        for (RestaurantCategoryEntity rcEntity : restaurantCategoryEntities) {
            categories.add(rcEntity.getCategoryEntity());
        }
        // java.util.Collections.sort(categories, Collator.getInstance());
        return categories;

    }

    public List<RestaurantCategoryEntity> getAllRestaurantsByCategory(CategoryEntity categoryEntity) {
        return restaurantCategoryDao.getAllRestaurantsByCategory(categoryEntity);
    }
}
