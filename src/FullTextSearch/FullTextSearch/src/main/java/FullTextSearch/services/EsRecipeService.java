package FullTextSearch.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EsRecipeService {
    private ElasticsearchOperations elasticsearchOperations;
    private static final String RECIPE_INDEX = "recipeindex";
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private EsRecipeRepository esRecipeRepository;
    @Autowired
    private TagServiceImpl tagService;

    public EsRecipe addRecipeToEs(Long recipeId) throws IOException {
        // creates and saves a recipe to the elastic search index
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Error: Recipe not found."));
        String tags = tagService.convertTagsToString(recipe);
        EsRecipe esRecipe = EsRecipe.builder()
                .name(recipe.getTitle())
                .tags(tags)
                .recipeId(recipe.getId())
                .thumbnailId(recipe.getThumbNailId())
                .mainPhotoId(recipe.getMainPhotoId())
                .build();
        esRecipeRepository.save(esRecipe);
        return esRecipe;
    }

    // pagable search results by name
    public List<EsRecipe> matchByName(String name) throws IOException {
        Query query = new StringQuery("{\"query\": {\"match\": {\"name\": \"" + name + "\"}}}");
        return esRecipeRepository.findByName(name);
    }

    public List<EsRecipe> autoSuggest(String name) {
        // autosuggest recipes based on what the user has types so far
        Query query = new StringQuery("{\"size\": 5, \"query\": {\"multi_match\": {\"query\":" + name + ", \"type\": \"bool_prefix\", \"fields\": [\"name\", \"name._2gram\", \"name._3gram\"]}}}");
        SearchHits<EsRecipe> searchHits = elasticsearchOperations.search(query, EsRecipe.class);
        List<EsRecipe> esRecipes= new ArrayList<>();
        for (SearchHit<EsRecipe> searchHit : searchHits) {
            EsRecipe esRecipe = searchHit.getContent();
            esRecipes.add(esRecipe);
        }
        return esRecipes;
    }
}
