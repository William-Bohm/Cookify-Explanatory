package FullTextSearch.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(indexName = "fooditemindex")
@Setting(replicas = 2, shards = 1)
public class EsFoodItem {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Keyword, name = "unit")
    private String unit;

    @Field(type = FieldType.Integer, name = "quantity", store = true)
    private Integer quantity;

    @Field(type = FieldType.Integer, name = "calories", store = true)
    private Integer calories;

    @Field(type = FieldType.Integer, name = "protein", store = true)
    private Integer protein;

    @Field(type = FieldType.Integer, name = "carbohydrates", store = true)
    private Integer carbohydrates;

    @Field(type = FieldType.Integer, name = "fat", store = true)
    private Integer fat;

}
