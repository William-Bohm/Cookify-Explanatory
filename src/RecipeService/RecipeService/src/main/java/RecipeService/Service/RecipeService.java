package RecipeService.Service;

import RecipeService.exceptions.RecipeNotFoundException;
import RecipeService.models.Direction;
import RecipeService.models.Ingredient;
import RecipeService.models.Recipe;
import RecipeService.models.Tag;
import RecipeService.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

//  Recipes
    public Mono<Recipe> createRecipe(Recipe recipe) {
        if (recipe == null || recipe.getTitle() == null || recipe.getTitle().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Title cannot be null or empty"));
        }
        // Generating UUID for the id
        String id = UUID.randomUUID().toString();
        recipe.setId(id);

        return recipeRepository.save(recipe);
    }

    public Mono<Recipe> updateRecipe(String recipeId, Recipe recipeUpdates) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (recipeUpdates == null) {
            return Mono.error(new IllegalArgumentException("Updates cannot be null"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(existingRecipe -> {
                    if (recipeUpdates.getTitle() != null) {
                        existingRecipe.setTitle(recipeUpdates.getTitle());
                    }
                    if (recipeUpdates.getDescription() != null) {
                        existingRecipe.setDescription(recipeUpdates.getDescription());
                    }
                    if (recipeUpdates.getCookTime() != null) {
                        existingRecipe.setCookTime(recipeUpdates.getCookTime());
                    }
                    if (recipeUpdates.getPrepTime() != null) {
                        existingRecipe.setPrepTime(recipeUpdates.getPrepTime());
                    }
                    if (recipeUpdates.getAdditionalTime() != null) {
                        existingRecipe.setAdditionalTime(recipeUpdates.getAdditionalTime());
                    }
                    if (recipeUpdates.getTotalTime() != null) {
                        existingRecipe.setTotalTime(recipeUpdates.getTotalTime());
                    }
                    if (recipeUpdates.getCharset() != null) {
                        existingRecipe.setCharset(recipeUpdates.getCharset());
                    }
                    if (recipeUpdates.getSourceUrl() != null) {
                        existingRecipe.setSourceUrl(recipeUpdates.getSourceUrl());
                    }
                    if (recipeUpdates.getAuthorId() != null) {
                        existingRecipe.setAuthorId(recipeUpdates.getAuthorId());
                    }
                    if (recipeUpdates.getOpenAiApiTokens() != null) {
                        existingRecipe.setOpenAiApiTokens(recipeUpdates.getOpenAiApiTokens());
                    }
                    if (recipeUpdates.getServings() != null) {
                        existingRecipe.setServings(recipeUpdates.getServings());
                    }
                    if (recipeUpdates.getReviewCount() != null) {
                        existingRecipe.setReviewCount(recipeUpdates.getReviewCount());
                    }
                    if (recipeUpdates.getReviewRating() != null) {
                        existingRecipe.setReviewRating(recipeUpdates.getReviewRating());
                    }
                    if (recipeUpdates.getCalories() != null) {
                        existingRecipe.setCalories(recipeUpdates.getCalories());
                    }
                    if (recipeUpdates.getFat() != null) {
                        existingRecipe.setFat(recipeUpdates.getFat());
                    }
                    if (recipeUpdates.getCarbs() != null) {
                        existingRecipe.setCarbs(recipeUpdates.getCarbs());
                    }
                    if (recipeUpdates.getProtein() != null) {
                        existingRecipe.setProtein(recipeUpdates.getProtein());
                    }
                    if (recipeUpdates.getMainPhotoId() != 0) {
                        existingRecipe.setMainPhotoId(recipeUpdates.getMainPhotoId());
                    }
                    if (recipeUpdates.getThumbNailId() != 0) {
                        existingRecipe.setThumbNailId(recipeUpdates.getThumbNailId());
                    }
                    if (recipeUpdates.getTags() != null) {
                        existingRecipe.setTags(recipeUpdates.getTags());
                    }
                    if (recipeUpdates.getPhoto() != null) {
                        existingRecipe.setPhoto(recipeUpdates.getPhoto());
                    }
                    if (recipeUpdates.getVideo() != null) {
                        existingRecipe.setVideo(recipeUpdates.getVideo());
                    }
                    if (recipeUpdates.getDirections() != null) {
                        existingRecipe.setDirections(recipeUpdates.getDirections());
                    }
                    if (recipeUpdates.getIngredients() != null) {
                        existingRecipe.setIngredients(recipeUpdates.getIngredients());
                    }
                    return recipeRepository.save(existingRecipe);
                });
    }

    public Mono<Void> deleteRecipe(String recipeId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(existingRecipe -> {
                    return recipeRepository.delete(existingRecipe);
                });
    }

    //    Directions
    public Mono<Recipe> addDirection(String recipeId, Direction direction) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (direction == null || direction.getStep().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Direction cannot be null or empty"));
        }
        int newStepNumber = Integer.parseInt(direction.getStepNumber());

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<Direction> directions = new ArrayList<>(recipe.getDirections());
                    directions.add(newStepNumber - 1, direction);  // add new direction at the specified step

                    // Shift subsequent directions one step up
                    for (int i = newStepNumber; i < directions.size(); i++) {
                        String stepNumber = Integer.toString(i + 1);
                        directions.get(i).setStepNumber(stepNumber);
                    }

                    recipe.setDirections(directions);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> changeDirection(String recipeId, String directionId, Direction newDirection) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (directionId == null || directionId.length() < 35
        ) {
            return Mono.error(new IllegalArgumentException("Direction ID cannot be null and must be a UUID"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<Direction> directions = recipe.getDirections();

                    for (int i = 0; i < directions.size(); i++) {
                        if (directions.get(i).getId().equals(directionId)) {
                            directions.set(i, newDirection);
                            break;
                        }
                    }

                    recipe.setDirections(directions);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> deleteDirection(String recipeId, String directionId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (directionId == null || directionId.length() < 35
        ) {
            return Mono.error(new IllegalArgumentException("Direction ID cannot be null and must be a UUID"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<Direction> directions = recipe.getDirections();

                    directions.removeIf(direction -> direction.getId().equals(directionId));

                    // Reorder remaining directions
                    for (int i = 0; i < directions.size(); i++) {
                        directions.get(i).setStepNumber(Integer.toString(i + 1));
                    }

                    recipe.setDirections(directions);
                    return recipeRepository.save(recipe);
                });
    }

// Ingredients
    public Mono<Recipe> addIngredient(String recipeId, Ingredient ingredient) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (ingredient == null || ingredient.getName().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Ingredient cannot be null or empty"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<Ingredient> ingredients = recipe.getIngredients();
                    ingredients.add(ingredient);
                    recipe.setIngredients(ingredients);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> deleteIngredient(String recipeId, String ingredientId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (ingredientId == null || ingredientId.length() < 35) {
            return Mono.error(new IllegalArgumentException("Ingredient ID cannot be null and must be a UUID"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<Ingredient> ingredients = recipe.getIngredients();
                    ingredients.removeIf(ingredient -> ingredient.getId().equals(ingredientId));
                    recipe.setIngredients(ingredients);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> changeIngredient(String recipeId, String ingredientId, Ingredient newIngredient) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (ingredientId == null || ingredientId.length() < 35) {
            return Mono.error(new IllegalArgumentException("Ingredient ID cannot be null and must be a UUID"));
        }
        if (newIngredient == null || newIngredient.getName().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("New Ingredient cannot be null or empty"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<Ingredient> ingredients = recipe.getIngredients();

                    for (int i = 0; i < ingredients.size(); i++) {
                        if (ingredients.get(i).getId().equals(ingredientId)) {
                            ingredients.set(i, newIngredient);
                            break;
                        }
                    }

                    recipe.setIngredients(ingredients);
                    return recipeRepository.save(recipe);
                });
    }


// Tags
    public Mono<Recipe> addTag(String recipeId, Tag tag) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (tag == null) {
            return Mono.error(new IllegalArgumentException("Tag cannot be null or empty"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<Tag> tags = recipe.getTags();
                    tags.add(tag);
                    recipe.setTags(tags);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> deleteTag(String recipeId, Tag tag) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (tag == null) {
            return Mono.error(new IllegalArgumentException("Tag cannot be null"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<Tag> tags = recipe.getTags();

                    tags.removeIf(existingTag -> existingTag == tag);

                    recipe.setTags(tags);
                    return recipeRepository.save(recipe);
                });
    }

//    Videos
    public Mono<Recipe> addVideo(String recipeId, String videoId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (videoId == null || videoId.length() < 35) {
            return Mono.error(new IllegalArgumentException("videoID cannot be null and must be a UUID"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<String> videos = recipe.getVideo();
                    videos.add(videoId);
                    recipe.setVideo(videos);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> deleteVideo(String recipeId, String videoId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (videoId == null || videoId.length() < 35) {
            return Mono.error(new IllegalArgumentException("videoID cannot be null and must be a UUID"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<String> videos = recipe.getVideo();

                    videos.removeIf(existingVideoId -> existingVideoId.equals(videoId));

                    recipe.setVideo(videos);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> changeVideo(String recipeId, String videoId, String newVideoId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (videoId == null || videoId.length() < 35) {
            return Mono.error(new IllegalArgumentException("videoID cannot be null and must be a UUID"));
        }
        if (newVideoId == null || newVideoId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("New Video ID cannot be null or empty"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<String> videos = recipe.getVideo();

                    for (int i = 0; i < videos.size(); i++) {
                        if (videos.get(i).equals(videoId)) {
                            videos.set(i, newVideoId);
                            break;
                        }
                    }

                    recipe.setVideo(videos);
                    return recipeRepository.save(recipe);
                });
    }


    //    Photos
    public Mono<Recipe> addPhoto(String recipeId, String photoId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (photoId == null || photoId.length() < 35) {
            return Mono.error(new IllegalArgumentException("photoId cannot be null and must be a UUID"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<String> photos = recipe.getPhoto();
                    photos.add(photoId);
                    recipe.setPhoto(photos);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> deletePhoto(String recipeId, String photoId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (photoId == null || photoId.length() < 35) {
            return Mono.error(new IllegalArgumentException("photoId cannot be null and must be a UUID"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<String> photos = recipe.getPhoto();

                    photos.removeIf(existingPhotoId -> existingPhotoId.equals(photoId));

                    recipe.setPhoto(photos);
                    return recipeRepository.save(recipe);
                });
    }

    public Mono<Recipe> changePhoto(String recipeId, String photoId, String newPhotoId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        if (photoId == null || photoId.length() < 35) {
            return Mono.error(new IllegalArgumentException("photoId cannot be null and must be a UUID"));
        }
        if (newPhotoId == null || newPhotoId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("New Photo ID cannot be null or empty"));
        }

        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .flatMap(recipe -> {
                    List<String> photos = recipe.getPhoto();

                    for (int i = 0; i < photos.size(); i++) {
                        if (photos.get(i).equals(photoId)) {
                            photos.set(i, newPhotoId);
                            break;
                        }
                    }

                    recipe.setPhoto(photos);
                    return recipeRepository.save(recipe);
                });
    }

//    specifics
    public Mono<Map<String, Object>> getNutritionalInfo(String recipeId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .map(recipe -> {
                    Map<String, Object> nutritionalInfo = new HashMap<>();
                    nutritionalInfo.put("protein", recipe.getProtein());
                    nutritionalInfo.put("carbs", recipe.getCarbs());
                    nutritionalInfo.put("fat", recipe.getFat());
                    nutritionalInfo.put("servings", recipe.getServings());
                    return nutritionalInfo;
                });
    }

    public Mono<Map<String, Object>> getReviewInfo(String recipeId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .map(recipe -> {
                    Map<String, Object> reviewInfo = new HashMap<>();
                    reviewInfo.put("reviewCount", recipe.getReviewCount());
                    reviewInfo.put("reviewRating", recipe.getReviewRating());
                    reviewInfo.put("servings", recipe.getServings());
                    return reviewInfo;
                });
    }

    public Mono<String> getAuthor(String recipeId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .map(Recipe::getAuthorId);
    }

    public Mono<Map<String, Object>> getTimeInfo(String recipeId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .map(recipe -> {
                    Map<String, Object> timeInfo = new HashMap<>();
                    timeInfo.put("cookTime", recipe.getCookTime());
                    timeInfo.put("prepTime", recipe.getPrepTime());
                    timeInfo.put("additionalTime", recipe.getAdditionalTime());
                    timeInfo.put("totalTime", recipe.getTotalTime());
                    return timeInfo;
                });
    }

    public Mono<Map<String, Object>> getMainInfo(String recipeId) {
        if (recipeId == null || recipeId.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Recipe ID cannot be null or empty"));
        }
        return recipeRepository.findById(recipeId)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe not found with id: " + recipeId)))
                .map(recipe -> {
                    Map<String, Object> mainInfo = new HashMap<>();
                    mainInfo.put("title", recipe.getTitle());
                    mainInfo.put("description", recipe.getDescription());
                    mainInfo.put("mainPhotoId", recipe.getMainPhotoId());
                    mainInfo.put("thumbNailId", recipe.getThumbNailId());
                    mainInfo.put("authorId", recipe.getAuthorId());
                    return mainInfo;
                });
    }
}
