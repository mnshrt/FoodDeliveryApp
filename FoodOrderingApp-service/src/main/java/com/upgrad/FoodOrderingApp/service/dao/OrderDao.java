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

    public OrderEntity createOrder(final OrderEntity orderEntity) {
        entityManager.persist(orderEntity);
        return orderEntity;
    }

    public CouponEntity findCouponByCouponName(final String coupon_name){
        try {
            String query = "select u from CouponEntity u where u.coupon_name = :coupon_name";
            return entityManager.createQuery(query, CouponEntity.class)
                    .setParameter("coupon_name", coupon_name).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CouponEntity findCouponByUUID(final String uuid) {
        try {
            String query = "select u from CouponEntity u where u.uuid = :uuid";
            return entityManager.createQuery(query, CouponEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<OrderEntity> findOrdersByCustomer(final int customer_id) {
        try {
            String query = "select u from OrderEntity u where u.customerEntity.id = :customer_id order by u.id";
            return entityManager.createQuery(query, OrderEntity.class)
                    .setParameter("customer_id", customer_id).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
