package FullTextSearch.services;


import FullTextSearch.models.EsFoodItem;
import FullTextSearch.repository.EsFoodItemRepository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EsFoodItemService {
    //    This service allows you to search an index with the elastic search query language
    private ElasticsearchOperations elasticsearchOperations;
    private static final String FOOD_ITEM_INDEX = "fooditemindex";
    @Autowired
    private EsFoodItemRepository esFoodItemRepository;

    public EsFoodItem createEsFoodItem(FullFoodItem foodItem) throws IOException {
        // creates and saves a food item to the elastic search index
        EsFoodItem esFoodItem = EsFoodItem.builder()
                .name(foodItem.getName())
                .unit(foodItem.getUnit())
                .quantity(foodItem.getQuantity())
                .calories(foodItem.getCalories())
                .fat(foodItem.getFat())
                .carbohydrates(foodItem.getCarbohydrates())
                .protein(foodItem.getProtein())
                .build();
        esFoodItemRepository.save(esFoodItem);
        return esFoodItem;
    }

    public List<EsFoodItem> matchByName(String name) throws IOException {
        // creates and saves a food item to the elastic search index
        return esFoodItemRepository.findByName(name);
    }

    public List<EsFoodItem> matchByNameAndUnit(String name, String unit) {
        // finds a food item by name and unit
        if (unit == null) {
            return esFoodItemRepository.findByName(name);
        } else {
            return esFoodItemRepository.findByNameAndUnit(name, unit);
        }
    }

    public EsFoodItem getSpecificFoodItem(String name, String unit) throws IOException {
        // with unit provided
        if (unit != null) {
            List<EsFoodItem> esFoodItems = matchByNameAndUnit(name, unit);
            if (esFoodItems.size() == 0) {
                return null;
            } else {
                for (EsFoodItem esFoodItem : esFoodItems) {
                    // check lengths to make sure we are not getting a substring
                    if (esFoodItem.getName().length() <= name.length() + 2) {
                        return esFoodItem;
                    }
                }
            }
        }
        // with no unit provided
        else {
            List<EsFoodItem> esFoodItems = matchByName(name);
            if (esFoodItems.size() == 0) {
                return null;
            } else {
                for (EsFoodItem esFoodItem : esFoodItems) {
                    if (esFoodItem.getName().length() <= name.length() + 2) {
                        return esFoodItem;
                    }
                }
            }
        }
        return null;
    }

    public FullFoodItem convertToFullFoodItem(EsFoodItem esFoodItem){
        return FullFoodItem.builder()
                .name(esFoodItem.getName())
                .unit(esFoodItem.getUnit())
                .quantity(esFoodItem.getQuantity())
                .calories(esFoodItem.getCalories())
                .fat(esFoodItem.getFat())
                .carbohydrates(esFoodItem.getCarbohydrates())
                .protein(esFoodItem.getProtein())
                .build();
    }
}
