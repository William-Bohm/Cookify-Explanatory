package FullTextSearch.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "recipeindex")
@Setting(replicas = 2, shards = 1)
public class EsRecipe {
    // this allows for search by name, or by tag (such as gluten_free)
    @Id
    private String id;

    @Field(type = FieldType.Search_As_You_Type, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "tags")
    private String tags;

    @Field(type = FieldType.Long, name = "recipeId", store = true)
    private Long recipeId;

    @Field(type = FieldType.Long, name = "thumbnailId", store = true)
    private Long thumbnailId;

    @Field(type = FieldType.Long, name = "mainPhotoId", store = true)
    private Long mainPhotoId;
}
