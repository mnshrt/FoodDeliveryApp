package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantityResponseItem;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.OrderItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Map.Entry;
@Controller
public class ItemController {

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantItemService restaurantItemService;


    // controller method for the getting top 5 items
    @CrossOrigin
    @GetMapping(path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ItemList>> getTopFiveItemsByPopularity(@PathVariable String restaurant_id)
            throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = restaurantService.getRestaurantByUuid(restaurant_id);

        List<RestaurantItemEntity> restaurantItemEntities = restaurantItemService.getItemsByRestaurant(restaurantEntity);

        Map<ItemEntity, Integer> idPriceMap = new HashMap<>();
        for (RestaurantItemEntity r : restaurantItemEntities) {

            OrderItemEntity orderItemEntity = orderItemService.getOrderItemEntityByItem(r.getItem());
            idPriceMap.put(orderItemEntity.getItem(), orderItemEntity.getPrice());
        }

        List<ItemEntity> itemEntities = getSortedItemList(idPriceMap);
        List<ItemList> itemListList = new ArrayList<>();
        for (ItemEntity ie : itemEntities) {
            int i = 0;
            while (i < 5) {
                ItemList itemQuantityResponseItem = new ItemList()
                        .itemName(ie.getItemName()).price(ie.getPrice()).id(UUID.fromString(ie.getUuid()))
                        .itemType(ItemList.ItemTypeEnum.fromValue(ie.getType()));
                itemListList.add(itemQuantityResponseItem);
                i++;
            }
        }
        return new ResponseEntity<>(itemListList, HttpStatus.OK);


    }

    // utility method to sort based on quantity
    private List<ItemEntity> getSortedItemList(Map<ItemEntity, Integer> unsortMap) {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        Set<Entry<ItemEntity, Integer>> set = unsortMap.entrySet();
        List<Entry<ItemEntity, Integer>> list = new ArrayList<Entry<ItemEntity, Integer>>(
                set);
        Collections.sort(list, new Comparator<Map.Entry<ItemEntity, Integer>>() {
            public int compare(Map.Entry<ItemEntity, Integer> o1,
                               Map.Entry<ItemEntity, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Entry<ItemEntity, Integer> entry : list) {
            itemEntityList.add(entry.getKey());

        }
        return itemEntityList;
    }
}
