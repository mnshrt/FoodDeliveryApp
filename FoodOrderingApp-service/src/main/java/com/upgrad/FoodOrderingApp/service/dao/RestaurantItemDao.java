package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantItemDao {


    @PersistenceContext
    private EntityManager entityManager;


    public List<RestaurantItemEntity> getItemsByRestaurant(RestaurantEntity re) {
        try {
            String query = "select u from RestaurantItemEntity u where u.restaurant = :userInput";
            return entityManager.createQuery(query, RestaurantItemEntity.class)
                    .setParameter("userInput", re).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
