package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    public OrderItemEntity getOrderItemEntityByItem(ItemEntity itemEntity) {

        try {
            String query = "select u from OrderItemEntity u where u.item = :userInput";
            return entityManager.createQuery(query, OrderItemEntity.class)
                    .setParameter("userInput", itemEntity).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
