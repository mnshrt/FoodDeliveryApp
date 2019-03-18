package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    CustomerService customerService;

    @CrossOrigin
    @GetMapping(path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@RequestHeader("authorization")
                                                                 final String access_token,
                                                                 @PathVariable final String coupon_name)
            throws AuthorizationFailedException, CouponNotFoundException {
        CustomerEntity customerEntity = customerService.getCustomer(access_token);

        if(String.valueOf(coupon_name).equals("null") || coupon_name.isEmpty()){
            throw new CouponNotFoundException("CPF-002",
                    "Coupon name field should not be empty");
        }

        CouponEntity couponEntity = orderService.getCouponByCouponName(coupon_name);

        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
        String uuid = couponEntity.getUuid();

        couponDetailsResponse.setCouponName(couponEntity.getCoupon_name());
        couponDetailsResponse.setId(UUID.fromString(uuid));
        couponDetailsResponse.setPercent(couponEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }
}
