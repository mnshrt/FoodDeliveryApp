package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantUpdatedResponse;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantCategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.junit.experimental.categories.Categories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantCategoryService restaurantCategoryService;

    @Autowired

    AddressService addressService;


    @GetMapping(path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {

        RestaurantListResponse restaurantListResponse = null;
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        List<RestaurantList> restaurantList = new ArrayList<>();
        if (!restaurantService.getAllRestaurants().isEmpty()) {

            restaurantEntities = restaurantService.getAllRestaurants();
            for (RestaurantEntity r : restaurantEntities) {

                //Get the categories
           /*     List<String> categories= restaurantCategoryService.getAllCategoriesByRestaurant(r);
                StringBuilder strBuilder = new StringBuilder("");
                for(int i=0;i< categories.size();i++){
                    if(i== categories.size()-1){
                    strBuilder.append(categories.get(i));
                    }else{
                        strBuilder.append(categories.get(i)+", ");
                    }
                }
                String categoriesString= strBuilder.toString();*/
                //RestaurantDetailsResponseAddress restaurantDetailsResponseAddress= addressService.getAddressById(r.getAddress());

                RestaurantList restaurant = new RestaurantList().id(UUID.fromString(r.getUuid()))

                        .numberCustomersRated(r.getNumberOfCustomersRated()).photoURL(r.getPhotoUrl())
                        .restaurantName(r.getRestaurantName()).averagePrice(r.getAveragePriceForTwo())
                        .customerRating(r.getCustomerRating()).categories("chinese");


                restaurantList.add(restaurant);
            }
            ;
        }

        return new ResponseEntity(new RestaurantListResponse().restaurants(restaurantList), HttpStatus.OK);
    }
/*
    @GetMapping(path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable String restaurant_name) {


    }
    @GetMapping(path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCategoryId(@PathVariable String category_id) {

    }
    @GetMapping(path = "/api/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByRestaurantId(@PathVariable String restaurant_id) {

    }
    @PutMapping(path="/api/restaurant/{restaurant_id}")
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(@PathVariable String restaurant_id){

    }

    */
}
