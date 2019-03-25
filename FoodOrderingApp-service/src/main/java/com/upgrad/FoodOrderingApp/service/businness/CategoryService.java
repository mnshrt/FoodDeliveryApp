package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    /**
     * @author Manish Rout (https://github.com/mnshrt)
     * Description - Service class for the Restaurant related methods
     */
    @Autowired
    CategoryDao categoryDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getAllCategories() {

        return categoryDao.getAllCategories();

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryEntity getCategoryByUuid(String category_id) throws CategoryNotFoundException {
        CategoryEntity ce = categoryDao.getCategoryByUuid(category_id);
        if (ce == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        } else {
            return ce;
        }
    }

}
