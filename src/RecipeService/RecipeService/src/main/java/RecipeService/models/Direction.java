package RecipeService.models;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Direction {
    private String id;

    @NotBlank
    private String step;

    @NotBlank
    private String stepNumber;
}
