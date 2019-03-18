package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryItemService {

    @Autowired
    CategoryItemDao categoryItemDao;

    public List<CategoryItemEntity> getCategoryItemEntityList(Integer id) {

        return categoryItemDao.getCategoryItemEntityList(id);

    }
}
