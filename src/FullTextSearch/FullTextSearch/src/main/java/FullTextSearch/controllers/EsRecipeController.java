package com.Cookify.ElasticSearch.controllers;

import com.Cookify.ElasticSearch.repository.EsRecipeRepository;
import com.Cookify.ElasticSearch.services.EsRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/es/recipe")
public class EsRecipeController {
    @Autowired
    private EsRecipeService esRecipeService;
    @Autowired
    private EsRecipeRepository esRecipeRepository;

    @PostMapping("/add/{recipeId}")
    public ResponseEntity addRecipeToEs(@PathVariable Long recipeId) throws IOException {
        try {
            esRecipeService.addRecipeToEs(recipeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
