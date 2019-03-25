package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Manish Rout (https://github.com/mnshrt)
 * Description - DAO class with operations for the category_item table
 */
@Repository
public class CategoryItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryItemEntity> getCategoryItemEntityList(Integer id) {

        try {
            String query = "select u from CategoryItemEntity u where u.id = :userInput";
            return entityManager.createQuery(query, CategoryItemEntity.class)
                    .setParameter("userInput", id).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<CategoryItemEntity> getCategoryItemEntityListByCategory(CategoryEntity categoryEntity) {

        try {
            String query = "select u from CategoryItemEntity u where u.category = :userInput";
            return entityManager.createQuery(query, CategoryItemEntity.class)
                    .setParameter("userInput", categoryEntity).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
