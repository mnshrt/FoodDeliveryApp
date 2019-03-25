package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manish Rout (https://github.com/mnshrt)
 * Description - DAO class with operations for the restaurant_category table
 */
@Repository
public class RestaurantCategoryDao {


    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantCategoryEntity> getAllCategoriesByRestaurant(RestaurantEntity restaurantEntity) {
        try {
            List restaurantcategory = new ArrayList();
            String query = "select r from RestaurantCategoryEntity r where r.restaurant = :userInput";
            restaurantcategory = entityManager.createQuery(query, RestaurantCategoryEntity.class)
                    .setParameter("userInput", restaurantEntity).getResultList();

            return restaurantcategory;

        } catch (NoResultException nre) {

            return new ArrayList<RestaurantCategoryEntity>();
        }
    }

    public List<RestaurantCategoryEntity> getAllRestaurantsByCategory(CategoryEntity categoryEntity) {
        try {
            List restaurantcategory = new ArrayList();
            String query = "select r from RestaurantCategoryEntity r where r.category = :userInput";
            restaurantcategory = entityManager.createQuery(query, RestaurantCategoryEntity.class)
                    .setParameter("userInput", categoryEntity).getResultList();

            return restaurantcategory;

        } catch (NoResultException nre) {

            return new ArrayList<RestaurantCategoryEntity>();
        }
    }
}
