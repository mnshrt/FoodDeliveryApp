package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class CategoryDao {
    @PersistenceContext
    private EntityManager entityManager;

    public String getCategoryName(CategoryEntity categoryEntity) {
        try {

            String query = "select u from CategoryEntity u where u.id = :userInput";
            return entityManager.createQuery(query, CategoryEntity.class)
                    .setParameter("userInput", categoryEntity.getId()).getSingleResult().getCategoryName();
        } catch (NoResultException nre) {

            return null;
        }
    }
}
