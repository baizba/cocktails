package com.semantic.web.controller;

import com.semantic.web.model.CocktailConcept;
import com.semantic.web.service.CocktailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryController {

    private final CocktailService cocktailService;

    public EntryController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @PutMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> createConcept(@RequestBody CocktailConcept cocktailConcept) {

        //validation
        if (cocktailConcept == null || StringUtils.isBlank(cocktailConcept.getPreferredLabel())) {
            return ResponseEntity.badRequest().build();
        }

        //insert to RDF
        try {
            cocktailService.insertConcept(cocktailConcept);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error occurred, cocktail not added, check server log");
        }

        return ResponseEntity.ok().build();
    }
}
