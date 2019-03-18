package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager entityManager;

    public CouponEntity findCouponByCouponName(final String coupon_name){
        try {
            String query = "select u from CouponEntity u where u.coupon_name = :coupon_name";
            return entityManager.createQuery(query, CouponEntity.class)
                    .setParameter("coupon_name", coupon_name).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
