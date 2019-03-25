package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    OrderItemDao orderItemDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity getOrderItemEntityByItem(ItemEntity item) {
        return orderItemDao.getOrderItemEntityByItem(item);
    }

    public List<OrderItemEntity> getOrderItemEntityByOrderId(int id) {
        return orderItemDao.getOrderItemEntityByOrderId(id);
    }
}
