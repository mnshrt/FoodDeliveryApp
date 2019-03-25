package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    @Autowired
    ItemDao itemDao;


    public ItemEntity getItemById(ItemEntity itemEntity) throws ItemNotFoundException {
        ItemEntity itemEntity1 = itemDao.getItemById(itemEntity.getId());
        if (itemEntity1 == null) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        } else {
            return itemEntity1;
        }

    }

    public ItemEntity getItemByUuid(UUID itemId) throws ItemNotFoundException {
        ItemEntity itemEntity1 = itemDao.getItemByUuid(itemId);
        if (itemEntity1 == null) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        } else {
            return itemEntity1;
        }
    }
}
