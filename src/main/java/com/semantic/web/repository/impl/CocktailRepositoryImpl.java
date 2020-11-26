package com.semantic.web.repository.impl;

import com.semantic.web.exception.ConceptSaveException;
import com.semantic.web.repository.CocktailRepository;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

@org.springframework.stereotype.Repository
public class CocktailRepositoryImpl implements CocktailRepository {

    private final Repository repository;

    public CocktailRepositoryImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void saveCocktail(Model model) throws ConceptSaveException {
        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch (Exception e) {
            throw new ConceptSaveException("error while saving cocktail to repository, check server logs", e);
        }
    }
}
