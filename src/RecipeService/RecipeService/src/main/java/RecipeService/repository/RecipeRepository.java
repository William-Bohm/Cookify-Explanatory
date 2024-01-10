package RecipeService.repository;

import RecipeService.models.Recipe;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RecipeRepository extends ReactiveCrudRepository<Recipe, String> {
    Mono<Recipe> findById(String id);
}
