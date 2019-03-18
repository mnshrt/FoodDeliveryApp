package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantDao {


    @PersistenceContext
    private EntityManager entityManager;


    public List<RestaurantEntity> getAllRestaurants() {

        try {
            //order by r.customerRating desc
            String query = "select r from RestaurantEntity r";
            return entityManager.createQuery(query, RestaurantEntity.class)
                    .getResultList();
        } catch (NoResultException nre) {

            return new ArrayList<RestaurantEntity>();
        }
    }


    public List<RestaurantEntity> getRestaurantByName(String restaurantName) {
        try {
            String query = "select r from RestaurantEntity r where r.restaurant like :name order by r.customer_rating desc";
            return entityManager.createQuery(query, RestaurantEntity.class)
                    .setParameter("name", '%' + restaurantName + '%').getResultList();

        } catch (NoResultException nre) {

            return new ArrayList<RestaurantEntity>();
        }
    }


    public RestaurantEntity getRestaurantById(String restaurantId) {
        try {
            String query = "select r from RestaurantEntity r where r.uuid = :uuid";
            return entityManager.createQuery(query, RestaurantEntity.class)
                    .setParameter("uuid", restaurantId).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }
    }


    public RestaurantEntity updateRestaurantDetails(RestaurantEntity restaurantEntity) {

        entityManager.merge(restaurantEntity);
        return restaurantEntity;

    }


/*
        public List<RestaurantEntity> getRestaurantByCategoryId(CategoryEntity categoryEntity) {


            try {

                String query = "select u from RestaurantEntity u where u.user = :categoryInput";
                return entityManager.createQuery(query, RestaurantEntity.class)
                        .setParameter("categoryInput", categoryEntity).getResultList();

            } catch (NoResultException nre) {

                return new ArrayList<RestaurantEntity>();
            }
        }*/


}
