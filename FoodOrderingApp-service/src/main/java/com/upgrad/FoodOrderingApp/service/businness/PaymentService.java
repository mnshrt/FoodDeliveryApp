package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
}
