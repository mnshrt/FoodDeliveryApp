package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryItemService {

    @Autowired
    CategoryItemDao categoryItemDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryItemEntity> getCategoryItemEntityList(Integer id) {

        return categoryItemDao.getCategoryItemEntityList(id);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryItemEntity> getCategoryItemEntityListByCategory(CategoryEntity categoryEntity) {

        return categoryItemDao.getCategoryItemEntityListByCategory(categoryEntity);

    }
}
