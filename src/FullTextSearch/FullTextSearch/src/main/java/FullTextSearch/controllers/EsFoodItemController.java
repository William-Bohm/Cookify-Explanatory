package com.Cookify.ElasticSearch.controllers;

import com.Cookify.ElasticSearch.models.EsFoodItem;
import com.Cookify.ElasticSearch.payload.request.FullFoodItem;
import com.Cookify.ElasticSearch.repository.EsFoodItemRepository;
import com.Cookify.ElasticSearch.services.EsFoodItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/es/fooditem")
@Slf4j
public class EsFoodItemController {
    @Autowired
    private EsFoodItemService esFoodItemService;
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private EsFoodItemRepository esFoodItemRepository;



    @GetMapping("/")
    public ResponseEntity<?> searchByNameAndUnit(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "unit", required = false) String unit){
        List<EsFoodItem> foodItems = esFoodItemService.matchByNameAndUnit(name, unit);
        if (foodItems.isEmpty()) {
            return new ResponseEntity<String>("No food items found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<EsFoodItem>>(foodItems, HttpStatus.OK);
    }

    @GetMapping("/specific")
    public ResponseEntity<?> getSpecific(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "unit", required = false) String unit){
        try {
            EsFoodItem foodItems = esFoodItemService.getSpecificFoodItem(name, unit);
            if (foodItems == null) {
                return new ResponseEntity<String>("No food items found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<EsFoodItem>(foodItems, HttpStatus.OK);
        } catch (IOException e) {
            log.error("error getting specific food item {}",e);
            return new ResponseEntity<String>("Error getting specific food item" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createFoodItem(@RequestBody FullFoodItem foodItem) {
        try {
            EsFoodItem createdFoodItem = esFoodItemService.createEsFoodItem(foodItem);
            FullFoodItem fullFoodItem = esFoodItemService.convertToFullFoodItem(createdFoodItem);
            return new ResponseEntity<FullFoodItem>(fullFoodItem, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("error creating food item {}",e);
            return new ResponseEntity<String>("Error creating food item" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/suggestions")
    @ResponseBody
    public List<String> fetchSuggestions(@RequestParam(value = "q", required = false) String query, @RequestParam(value = "page", required = false) String page ){
        log.info("fetch suggests {}",query);
        List<String> suggests = esFoodItemService.fetchSuggestions(query, page);
        log.info("suggests {}",suggests);
        return suggests;
    }

//    @PostMapping("/")
//    public ResponseEntity<?> createFoodItem(@RequestBody FullFoodItem foodItem) {
//        log.info("creating food item {}",foodItem);
//        try {
//
//            EsFoodItem createdFoodItem = esFoodItemService.createEsFoodItem(foodItem);
//            return new ResponseEntity<EsFoodItem>(createdFoodItem, HttpStatus.CREATED);
//        } catch (Exception e) {
//            log.error("error creating food item {}",e);
//            return new ResponseEntity<String>("Error creating food item", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
