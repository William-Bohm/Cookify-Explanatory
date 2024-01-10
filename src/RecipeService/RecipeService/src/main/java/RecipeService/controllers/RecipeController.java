package RecipeService.controllers;

import RecipeService.models.Direction;
import RecipeService.models.Ingredient;
import RecipeService.models.Recipe;
import RecipeService.Service.RecipeService;
import RecipeService.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/cookify/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

//    Recipe
    @PostMapping("/")
    public Mono<ResponseEntity<Recipe>> createRecipe(@Valid @RequestBody Recipe recipe) {
        return recipeService.createRecipe(recipe)
                .map(newRecipe -> ResponseEntity.ok(newRecipe));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Recipe>> updateRecipe(@PathVariable String id, @Valid @RequestBody Recipe recipe) {
        return recipeService.updateRecipe(id, recipe)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe));
    }

    @DeleteMapping("/{recipeId}")
    public Mono<Void> deleteRecipe(@PathVariable String recipeId) {
        return recipeService.deleteRecipe(recipeId);
    }

    //    Directions
    @PostMapping("/{id}/directions")
    public Mono<ResponseEntity<Recipe>> addDirection(@PathVariable String id, @Valid @RequestBody Direction direction) {
        return recipeService.addDirection(id, direction)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{recipeId}/directions/{directionId}")
    public Mono<ResponseEntity<Recipe>> deleteDirection(@PathVariable String recipeId, @PathVariable String directionId) {
        return recipeService.deleteDirection(recipeId, directionId)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{recipeId}/directions/{directionId}")
    public Mono<ResponseEntity<Recipe>> changeDirection(
            @PathVariable String recipeId,
            @PathVariable String directionId,
            @RequestBody Direction newDirection
    ) {
        return recipeService.changeDirection(recipeId, directionId, newDirection)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

//    Ingredients
    @PostMapping("/{id}/ingredients")
    public Mono<ResponseEntity<Recipe>> addIngredient(@PathVariable String id, @Valid @RequestBody Ingredient ingredient) {
        return recipeService.addIngredient(id, ingredient)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{recipeId}/ingredients/{ingredientId}")
    public Mono<ResponseEntity<Recipe>> deleteIngredient(
            @PathVariable String recipeId,
            @PathVariable String ingredientId
    ) {
        return recipeService.deleteIngredient(recipeId, ingredientId)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{recipeId}/ingredients/{ingredientId}")
    public Mono<ResponseEntity<Recipe>> changeIngredient(
            @PathVariable String recipeId,
            @PathVariable String ingredientId,
            @RequestBody Ingredient newIngredient
    ) {
        return recipeService.changeIngredient(recipeId, ingredientId, newIngredient)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

//    Tags
    @PostMapping("/{id}/tags")
    public Mono<ResponseEntity<Recipe>> addTag(@PathVariable String id, @Valid @RequestBody Tag tag) {
        return recipeService.addTag(id, tag)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/tags")
    public Mono<ResponseEntity<Recipe>> deleteTag(@PathVariable String id, @Valid @RequestBody Tag tag) {
        return recipeService.deleteTag(id, tag)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

//    Videos
    @PostMapping("/{id}/videos")
    public Mono<ResponseEntity<Recipe>> addVideo(@PathVariable String id, @RequestBody String video) {
        return recipeService.addVideo(id, video)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{recipeId}/videos/{videoId}")
    public Mono<ResponseEntity<Recipe>> deleteVideo(
            @PathVariable String recipeId,
            @PathVariable String videoId
    ) {
        return recipeService.deleteVideo(recipeId, videoId)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{recipeId}/videos/{videoId}")
    public Mono<ResponseEntity<Recipe>> changeVideo(
            @PathVariable String recipeId,
            @PathVariable String videoId,
            @RequestBody String newVideoId
    ) {
        return recipeService.changeVideo(recipeId, videoId, newVideoId)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

//    Photos
    @PostMapping("/{id}/photos")
    public Mono<ResponseEntity<Recipe>> addPhoto(@PathVariable String recipeID, @RequestBody String photo) {
        return recipeService.addPhoto(recipeID, photo)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{recipeId}/photos/{photoId}")
    public Mono<ResponseEntity<Recipe>> deletePhoto(
            @PathVariable String recipeId,
            @PathVariable String photoId
    ) {
        return recipeService.deletePhoto(recipeId, photoId)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{recipeId}/photos/{photoId}")
    public Mono<ResponseEntity<Recipe>> changePhoto(
            @PathVariable String recipeId,
            @PathVariable String photoId,
            @RequestBody String newPhotoId
    ) {
        return recipeService.changePhoto(recipeId, photoId, newPhotoId)
                .map(updatedRecipe -> ResponseEntity.ok(updatedRecipe))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

//    Specifics

    @GetMapping("/{id}/nutritional-info")
    public Mono<ResponseEntity<Map<String, Object>>> getNutritionalInfo(@PathVariable("id") String recipeId) {
        return recipeService.getNutritionalInfo(recipeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/review-info")
    public Mono<ResponseEntity<Map<String, Object>>> getReviewInfo(@PathVariable("id") String recipeId) {
        return recipeService.getReviewInfo(recipeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/author")
    public Mono<ResponseEntity<String>> getAuthor(@PathVariable("id") String recipeId) {
        return recipeService.getAuthor(recipeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/time-info")
    public Mono<ResponseEntity<Map<String, Object>>> getTimeInfo(@PathVariable("id") String recipeId) {
        return recipeService.getTimeInfo(recipeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/main-info")
    public Mono<ResponseEntity<Map<String, Object>>> getMainInfo(@PathVariable("id") String recipeId) {
        return recipeService.getMainInfo(recipeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
