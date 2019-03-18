package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name="uuid")
    @Size(max = 200)
    private String uuid;
    @NotNull
    @Column(name = "bill")
    private BigDecimal bill;
    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private CouponEntity couponEntity;
    @Column(name = "discount")
    private BigDecimal discount;
    @NotNull
    @Column(name = "date")
    private ZonedDateTime date;
    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private PaymentEntity paymentEntity;
    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @NotNull
    private CustomerEntity customerEntity;
    @ManyToOne (fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @NotNull
    private AddressEntity addressEntity;
    @NotNull
    @Column(name = "restaurant_id")
    private int restaurant_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public CouponEntity getCouponEntity() {
        return couponEntity;
    }

    public void setCouponEntity(CouponEntity couponEntity) {
        this.couponEntity = couponEntity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public PaymentEntity getPaymentEntity() {
        return paymentEntity;
    }

    public void setPaymentEntity(PaymentEntity paymentEntity) {
        this.paymentEntity = paymentEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public AddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public OrderEntity(){

    }
}
