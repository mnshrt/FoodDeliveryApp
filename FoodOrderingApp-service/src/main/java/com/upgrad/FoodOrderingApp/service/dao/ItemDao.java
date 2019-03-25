package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemById(int id) {

        try {

            String query = "select u from ItemEntity u where u.id = :userInput";
            return entityManager.createQuery(query, ItemEntity.class)
                    .setParameter("userInput", id).getSingleResult();

        } catch (NoResultException nre) {

            return null;
        }
    }
}
