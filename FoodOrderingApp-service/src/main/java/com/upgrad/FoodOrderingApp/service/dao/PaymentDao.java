package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PaymentEntity> findAllPaymentMethods(){
        try{
            String query = "select u from PaymentEntity u";
            return entityManager.createQuery(query, PaymentEntity.class)
                    .getResultList();
        } catch(NoResultException nre) {
            return null;
        }
    }
}
