package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manish Rout (https://github.com/mnshrt)
 * Description - DAO class with operations for the restaurant table
 */
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

    // adding lower to make case insensitive search
    public List<RestaurantEntity> getAllRestaurantsByName(String restaurantName) {
        try {
            String query = "select r from RestaurantEntity r where lower(r.restaurantName) like lower(:name) order by r.customerRating desc";
            return entityManager.createQuery(query, RestaurantEntity.class)
                    .setParameter("name", '%' + restaurantName + '%').getResultList();

        } catch (NoResultException nre) {

            return new ArrayList<RestaurantEntity>();
        }
    }


    public RestaurantEntity getRestaurantByUuid(String restaurantId) {
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


    public RestaurantEntity getRestaurantById(RestaurantEntity restaurantEntity) {


        try {

            String query = "select u from RestaurantEntity u where u.id = :userInput";
            return entityManager.createQuery(query, RestaurantEntity.class)
                    .setParameter("userInput", restaurantEntity.getId()).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }
    }


}
