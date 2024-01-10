package RecipeService.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Document(collection = "recipe")
public class Recipe {
    @Id
    private String id;
    @NotBlank
    private String title;
    private String description;
    private String cookTime;
    private String prepTime;
    private String additionalTime;
    private String totalTime;
    private String charset;
    private String sourceUrl;
    private String authorId;
    private String openAiApiTokens;
    private Integer servings;
    private Integer reviewCount;
    private Double reviewRating;
    private Integer calories;
    private Double fat;
    private Double carbs;
    private Double protein;
    private int mainPhotoId;
    private int thumbNailId;
    private List<Tag> tags;
    private List<String> photo;
    private List<String> video;
    private List<Direction> directions;
    private List<Ingredient> ingredients;
}
