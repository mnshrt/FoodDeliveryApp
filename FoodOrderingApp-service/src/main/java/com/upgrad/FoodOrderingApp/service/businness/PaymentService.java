package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Description - Service class for payment related methods.
 */

@Service
public class PaymentService {

    @Autowired
    PaymentDao paymentDao;

    public List<PaymentEntity> getAllPaymentMethods(){
        List<PaymentEntity> paymentEntityList = paymentDao.findAllPaymentMethods();
        if (paymentEntityList == null){
            return Collections.emptyList();
        } else {
            return paymentEntityList;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentEntity getPaymentByUUID(String uuid) throws PaymentMethodNotFoundException {
        PaymentEntity paymentEntity = paymentDao.findPaymentByUuid(uuid);
        if (paymentEntity == null) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        }
        return paymentEntity;
    }
}
