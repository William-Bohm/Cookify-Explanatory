package FullTextSearch.repository;

import FullTextSearch.models.EsRecipe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsRecipeRepository extends ElasticsearchRepository<EsRecipe, String> {
    List<EsRecipe> findByName(String name);
    List<EsRecipe> findByNameAndTags(String name, String tags);
    List<EsRecipe> findByTags(String tags);
}
