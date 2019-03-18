package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;

    public List<CategoryEntity> getAllCategories() {

        return categoryDao.getAllCategories();
    }

    public CategoryEntity getCategoryById(String category_id) {

        return categoryDao.getCategoryById(category_id);
    }

 /*   public String getAllCategoriesByRestaurantId(RestaurantEntity r) {

        categoryDao.getAllCategoriesByRestaurantId(r).toString();
    }
    */
}
