package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
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
    CustomerService customerService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantCategoryService restaurantCategoryService;

    @Autowired
    AddressService addressService;

    @Autowired
    CategoryItemService categoryItemService;

    @Autowired
    ItemService itemService;

    @Autowired
    CategoryService categoryService;


    @GetMapping(path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {

        RestaurantListResponse restaurantListResponse = null;
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        List<RestaurantList> restaurantList = new ArrayList<>();
        if (!restaurantService.getAllRestaurants().isEmpty()) {

            restaurantEntities = restaurantService.getAllRestaurants();
            for (RestaurantEntity r : restaurantEntities) {

                //Get the categories
                List<String> categories = restaurantCategoryService.getAllCategoriesByRestaurant(r);
                StringBuilder strBuilder = new StringBuilder("");
                for(int i=0;i< categories.size();i++){
                    if(i== categories.size()-1){
                    strBuilder.append(categories.get(i));
                    }else{
                        strBuilder.append(categories.get(i)+", ");
                    }
                }
                String categoriesString = strBuilder.toString();

                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
                RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();

                AddressEntity addressEntity = addressService.getAddressById(r.getAddress());

                StateEntity stateEntity = addressService.getStateById(addressEntity.getStateEntity());
                restaurantDetailsResponseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
                restaurantDetailsResponseAddressState.setStateName(stateEntity.getState_name());

                restaurantDetailsResponseAddress.city(addressEntity.getCity());
                restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());
                restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlat_buil_number());
                restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
                restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
                restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

                RestaurantList restaurant = new RestaurantList().id(UUID.fromString(r.getUuid()))
                        .address(restaurantDetailsResponseAddress)
                        .numberCustomersRated(r.getNumberOfCustomersRated()).photoURL(r.getPhotoUrl())
                        .restaurantName(r.getRestaurantName()).averagePrice(r.getAveragePriceForTwo())
                        .customerRating(r.getCustomerRating()).categories(categoriesString);


                restaurantList.add(restaurant);
            }
            ;
        }

        return new ResponseEntity(new RestaurantListResponse().restaurants(restaurantList), HttpStatus.OK);


    }
    @GetMapping(path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable String restaurant_name) throws RestaurantNotFoundException {
        if (restaurant_name == null) {
            throw new RestaurantNotFoundException("RF-003", "Restaurant name field should not be empty");

        } else {


            RestaurantListResponse restaurantListResponse = null;
            List<RestaurantEntity> restaurantEntities = new ArrayList<>();
            List<RestaurantList> restaurantList = new ArrayList<>();
            if (!restaurantService.getAllRestaurants().isEmpty()) {

                restaurantEntities = restaurantService.getAllRestaurantsByName(restaurant_name);
                for (RestaurantEntity r : restaurantEntities) {

                    //Get the categories
                    List<String> categories = restaurantCategoryService.getAllCategoriesByRestaurant(r);
                    StringBuilder strBuilder = new StringBuilder("");
                    for (int i = 0; i < categories.size(); i++) {
                        if (i == categories.size() - 1) {
                            strBuilder.append(categories.get(i));
                        } else {
                            strBuilder.append(categories.get(i) + ", ");
                        }
                    }
                    String categoriesString = strBuilder.toString();

                    RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
                    RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();

                    AddressEntity addressEntity = addressService.getAddressById(r.getAddress());

                    StateEntity stateEntity = addressService.getStateById(addressEntity.getStateEntity());
                    restaurantDetailsResponseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
                    restaurantDetailsResponseAddressState.setStateName(stateEntity.getState_name());

                    restaurantDetailsResponseAddress.city(addressEntity.getCity());
                    restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());
                    restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlat_buil_number());
                    restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
                    restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
                    restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

                    RestaurantList restaurant = new RestaurantList().id(UUID.fromString(r.getUuid()))
                            .address(restaurantDetailsResponseAddress)
                            .numberCustomersRated(r.getNumberOfCustomersRated()).photoURL(r.getPhotoUrl())
                            .restaurantName(r.getRestaurantName()).averagePrice(r.getAveragePriceForTwo())
                            .customerRating(r.getCustomerRating()).categories(categoriesString);


                    restaurantList.add(restaurant);
                }
                ;
            }

            return new ResponseEntity(new RestaurantListResponse().restaurants(restaurantList), HttpStatus.OK);

        }


    }

    @GetMapping(path = "/api/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByRestaurantId(@PathVariable String restaurant_id) throws RestaurantNotFoundException {

        RestaurantDetailsResponse restaurantDetailsResponse = null;
        if (restaurant_id == null) {
            throw new RestaurantNotFoundException("RF-002", "Restaurant id field should not be empty");

        } else {
            RestaurantEntity r = null;
            if (restaurantService.getRestaurantById(restaurant_id) != null) {

                r = restaurantService.getRestaurantById(restaurant_id);


                //Get the categories
                List<CategoryList> categoryListList = new ArrayList<>();
                List<CategoryEntity> categories = restaurantCategoryService.getAllCategoryEntitiesByRestaurant(r);

                for (CategoryEntity c : categories) {
                    List<CategoryItemEntity> categoryItemEntities = categoryItemService.getCategoryItemEntityListByCategory(c);
                    List<ItemEntity> itemEntityList = new ArrayList<>();
                    for (CategoryItemEntity cie : categoryItemEntities) {
                        itemEntityList.add(itemService.getItemById(cie.getItem()));
                    }
                    List<ItemList> itemListList = new ArrayList<>();
                    for (ItemEntity ie : itemEntityList) {
                        ItemList itemList = new ItemList().id(UUID.fromString(ie.getUuid())).itemName(ie.getItemName())
                                .price(ie.getPrice()).itemType(ItemList.ItemTypeEnum.valueOf(ie.getType()));
                        itemListList.add(itemList);
                    }

                    CategoryList categoryList = new CategoryList()
                            .id(UUID.fromString(c.getUuid())).categoryName(c.getCategoryName()).itemList(itemListList);
                    categoryListList.add(categoryList);

                }
                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
                RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();

                AddressEntity addressEntity = addressService.getAddressById(r.getAddress());

                StateEntity stateEntity = addressService.getStateById(addressEntity.getStateEntity());
                restaurantDetailsResponseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
                restaurantDetailsResponseAddressState.setStateName(stateEntity.getState_name());

                restaurantDetailsResponseAddress.city(addressEntity.getCity());
                restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());
                restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlat_buil_number());
                restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
                restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
                restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

                restaurantDetailsResponse = new RestaurantDetailsResponse().id(UUID.fromString(r.getUuid()))
                        .address(restaurantDetailsResponseAddress)
                        .numberCustomersRated(r.getNumberOfCustomersRated()).photoURL(r.getPhotoUrl())
                        .restaurantName(r.getRestaurantName()).averagePrice(r.getAveragePriceForTwo())
                        .customerRating(r.getCustomerRating()).categories(categoryListList);


            }

        }

        return new ResponseEntity(restaurantDetailsResponse, HttpStatus.OK);


    }

    @GetMapping(path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCategoryId(@PathVariable String category_id) {
        CategoryEntity categoryEntity = categoryService.getCategoryById(category_id);

        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryService.getAllRestaurantsByCategory(categoryEntity);
        List<RestaurantEntity> restaurantEntityList = new ArrayList<>();
        for (RestaurantCategoryEntity r : restaurantCategoryEntities) {
            RestaurantEntity restaurantEntity = restaurantService.getRestaurantByRestaurantId(r.getRestaurantEntity());
            restaurantEntityList.add(restaurantEntity);

        }
        List<RestaurantList> restaurantList = new ArrayList<>();
        if (!restaurantEntityList.isEmpty()) {

            for (RestaurantEntity r : restaurantEntityList) {

                //Get the categories
                List<String> categories = restaurantCategoryService.getAllCategoriesByRestaurant(r);
                StringBuilder strBuilder = new StringBuilder("");
                for (int i = 0; i < categories.size(); i++) {
                    if (i == categories.size() - 1) {
                        strBuilder.append(categories.get(i));
                    } else {
                        strBuilder.append(categories.get(i) + ", ");
                    }
                }
                String categoriesString = strBuilder.toString();

                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
                RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();

                AddressEntity addressEntity = addressService.getAddressById(r.getAddress());

                StateEntity stateEntity = addressService.getStateById(addressEntity.getStateEntity());
                restaurantDetailsResponseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
                restaurantDetailsResponseAddressState.setStateName(stateEntity.getState_name());

                restaurantDetailsResponseAddress.city(addressEntity.getCity());
                restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());
                restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlat_buil_number());
                restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
                restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
                restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

                RestaurantList restaurant = new RestaurantList().id(UUID.fromString(r.getUuid()))
                        .address(restaurantDetailsResponseAddress)
                        .numberCustomersRated(r.getNumberOfCustomersRated()).photoURL(r.getPhotoUrl())
                        .restaurantName(r.getRestaurantName()).averagePrice(r.getAveragePriceForTwo())
                        .customerRating(r.getCustomerRating()).categories(categoriesString);


                restaurantList.add(restaurant);
            }

        }

        return new ResponseEntity(new RestaurantListResponse().restaurants(restaurantList), HttpStatus.OK);



    }


    @PutMapping(path="/api/restaurant/{restaurant_id}")
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(@RequestHeader("authorization") final String access_token, @RequestParam double customer_rating, @PathVariable String restaurant_id)
            throws RestaurantNotFoundException, AuthorizationFailedException, InvalidRatingException {

        if (customerService.checkCustomer(access_token)) {

            RestaurantUpdatedResponse restaurantUpdatedResponse = null;
            if (restaurant_id == null) {
                throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
            } else {
                RestaurantEntity restaurantEntity = restaurantService.getRestaurantById(restaurant_id);
                if (restaurantEntity == null) {
                    throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
                } else {
                    if (customer_rating >= 1 && customer_rating <= 5) {
                        int updatedNumber = restaurantEntity.getNumberOfCustomersRated() + 1;
                        restaurantEntity.setCustomerRating(BigDecimal.valueOf(customer_rating));
                        restaurantEntity.setNumberOfCustomersRated(updatedNumber);
                        RestaurantEntity updatedRestaurantEntity = restaurantService.updateRestaurantDetails(restaurantEntity);
                        restaurantUpdatedResponse = new RestaurantUpdatedResponse().id(UUID.fromString(updatedRestaurantEntity.getUuid()))
                                .status("RESTAURANT RATING UPDATED SUCCESSFULLY");
                    } else {
                        throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
                    }

                }
            }
            return new ResponseEntity<>(restaurantUpdatedResponse, HttpStatus.OK);
        } else {

            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
    }

}