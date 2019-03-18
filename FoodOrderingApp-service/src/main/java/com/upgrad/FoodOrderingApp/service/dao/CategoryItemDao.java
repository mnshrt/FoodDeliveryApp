package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

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
}
