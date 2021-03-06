package com.semantic.web.service;

import com.semantic.web.model.CocktailConcept;

public interface CocktailService {

    void insertConcept(final CocktailConcept cocktailConcept);

    String getAllConcepts();

    String getConcept(final String preferredLabel, String accept);

    void deleteConcept(final String preferredLabel);
}
