package com.semantic.web.repository;

import com.semantic.web.exception.ConceptSaveException;
import org.eclipse.rdf4j.model.Model;

public interface CocktailRepository {

    void saveCocktail(Model model) throws ConceptSaveException;

    Model getAllConcepts();
}
