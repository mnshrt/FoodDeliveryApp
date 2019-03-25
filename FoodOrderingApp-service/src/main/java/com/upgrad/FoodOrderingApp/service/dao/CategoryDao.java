package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


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

    public List<CategoryEntity> getAllCategories() {
        try {

            String query = "select u from CategoryEntity u order by u.categoryName";
            return entityManager.createQuery(query, CategoryEntity.class)
                    .getResultList();
        } catch (NoResultException nre) {

            return null;
        }
    }

    public CategoryEntity getCategoryByUuid(String category_id) {
        try {

            String query = "select u from CategoryEntity u where u.uuid = :userInput";
            return entityManager.createQuery(query, CategoryEntity.class)
                    .setParameter("userInput", category_id).getSingleResult();
        } catch (NoResultException nre) {

            return null;
        }
    }
}
