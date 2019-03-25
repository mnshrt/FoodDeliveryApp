package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryItemService;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryItemService categoryItemService;

    @Autowired
    ItemService itemService;

    // controller method for the get all categories endpoint
    @CrossOrigin
    @GetMapping(path = "/category/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories() {

        List<CategoryEntity> categoryEntityList = categoryService.getAllCategories();

        List<CategoryListResponse> categoriesResponseList = new ArrayList<CategoryListResponse>();
        if (!categoryEntityList.isEmpty()) {

            for (CategoryEntity n : categoryEntityList) {
                CategoryListResponse categoryListResponse = new CategoryListResponse();
                categoryListResponse.setId(UUID.fromString(n.getUuid()));
                categoryListResponse.setCategoryName(n.getCategoryName());

                categoriesResponseList.add(categoryListResponse);
            }

        }

        return new ResponseEntity<>(new CategoriesListResponse().categories(categoriesResponseList), HttpStatus.OK);

    }


    // controller method for the get category by id endpoint
    @CrossOrigin
    @GetMapping(path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable String category_id) throws CategoryNotFoundException, ItemNotFoundException {

        if (category_id == null) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        } else {

            CategoryEntity categoryEntity = categoryService.getCategoryByUuid(category_id);

            CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse();
            categoryDetailsResponse.setId(UUID.fromString(categoryEntity.getUuid()));
            categoryDetailsResponse.setCategoryName(categoryEntity.getCategoryName());


            List<CategoryItemEntity> categoryItemEntityList = categoryItemService.getCategoryItemEntityList(categoryEntity.getId());
            List<ItemList> itemListForCategory = new ArrayList<>();

            for (CategoryItemEntity n : categoryItemEntityList) {
                ItemList itemList = new ItemList();
                ItemEntity itemEntity = itemService.getItemById(n.getItem());
                itemList.setId(UUID.fromString(itemEntity.getUuid()));
                itemList.setItemName(itemEntity.getItemName());
                itemList.setPrice(itemEntity.getPrice());

                itemList.setItemType(ItemList.ItemTypeEnum.valueOf(itemEntity.getType()));
                itemListForCategory.add(itemList);

            }

            //fetching the item list

            categoryDetailsResponse.setItemList(itemListForCategory);

            return new ResponseEntity<>(categoryDetailsResponse, HttpStatus.OK);


        }
    }
}
