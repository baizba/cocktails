package com.semantic.web.repository.impl;

import com.semantic.web.exception.ConceptDeleteException;
import com.semantic.web.exception.ConceptSaveException;
import com.semantic.web.repository.CocktailRepository;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.util.Repositories;

@org.springframework.stereotype.Repository
public class CocktailRepositoryImpl implements CocktailRepository {

    private static final String PREF_LABEL_PLACEHOLDER = "${prefLabel}";
    private static final String COCKTAIL_NAMESPACE = "http://example.org/cocktail#";

    private final Repository repository;
    private final String conceptQuery;
    private final String allConcpetsQuery;

    public CocktailRepositoryImpl(final Repository repository, final String conceptQuery, final String allConcpetsQuery) {
        this.repository = repository;
        this.conceptQuery = conceptQuery;
        this.allConcpetsQuery = allConcpetsQuery;
    }

    @Override
    public void saveCocktail(Model model) throws ConceptSaveException {
        try (RepositoryConnection connection = repository.getConnection()) {
            connection.add(model);
        } catch (Exception e) {
            throw new ConceptSaveException("error while saving cocktail to repository, check server logs", e);
        }
    }

    @Override
    public Model getAllConcepts() {
        return Repositories.graphQuery(repository, allConcpetsQuery, r -> QueryResults.asModel(r));
    }

    @Override
    public Model getConcept(final String preferredLabel) {
        final String query = conceptQuery.replace(PREF_LABEL_PLACEHOLDER, preferredLabel);
        return Repositories.graphQuery(repository, query, r -> QueryResults.asModel(r));
    }

    @Override
    public void deleteConcept(String preferredLabel) {
        try (RepositoryConnection connection = repository.getConnection()) {
            final IRI iri = repository.getValueFactory().createIRI(COCKTAIL_NAMESPACE + preferredLabel);
            final RepositoryResult<Statement> cocktailStatements = connection.getStatements(iri, null, null);
            connection.remove(cocktailStatements);
        } catch (Exception e) {
            throw new ConceptDeleteException("error while deleting cocktail, check server logs", e);
        }
    }
}
