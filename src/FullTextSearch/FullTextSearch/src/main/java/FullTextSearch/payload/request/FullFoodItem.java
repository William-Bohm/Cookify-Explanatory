package com.Cookify.ElasticSearch.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FullFoodItem {
    private String name;
    private String unit;
    private Integer quantity;
    private Integer calories;
    private Integer protein;
    private Integer carbohydrates;
    private Integer fat;
}
