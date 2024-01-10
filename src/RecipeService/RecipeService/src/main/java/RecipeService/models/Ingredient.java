package RecipeService.models;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Ingredient {
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String amount;

    private String fullText;
    private String simplifiedText;
    private String foodItemId;
}
