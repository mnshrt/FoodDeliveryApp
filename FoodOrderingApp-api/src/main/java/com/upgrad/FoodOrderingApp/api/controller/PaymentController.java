package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @CrossOrigin
    @GetMapping(path = "/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PaymentListResponse> getPaymentMethods(){
        List<PaymentEntity> paymentEntityList = paymentService.getAllPaymentMethods();

        PaymentListResponse paymentListResponse = new PaymentListResponse();

        if(paymentEntityList.size() > 0){
            for (int i = 0; i < paymentEntityList.size(); i++){
                PaymentResponse paymentResponse = new PaymentResponse();
                String uuid = paymentEntityList.get(i).getUuid();

                paymentResponse.setId(UUID.fromString(uuid));
                paymentResponse.setPaymentName(paymentEntityList.get(i).getPayment_name());
                paymentListResponse.addPaymentMethodsItem(paymentResponse);
            }
        } else {
            paymentListResponse.setPaymentMethods(Collections.emptyList());
        }

        return new ResponseEntity<PaymentListResponse>(paymentListResponse, HttpStatus.OK);
    }
}
