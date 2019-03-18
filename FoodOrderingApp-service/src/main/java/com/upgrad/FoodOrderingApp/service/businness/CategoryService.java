package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryService {

    @Autowired
    CategoryDao categoryDao;

 /*   public String getAllCategoriesByRestaurantId(RestaurantEntity r) {

        categoryDao.getAllCategoriesByRestaurantId(r).toString();
    }
    */
}
