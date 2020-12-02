package com.semantic.web.service.impl;

import com.semantic.web.model.CocktailConcept;
import com.semantic.web.repository.CocktailRepository;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CocktailServiceImplTest {

    private static final String COCKTAIL_LABEL = "cocktailLabel";

    @Mock
    private CocktailRepository cocktailRepository;

    @InjectMocks
    private CocktailServiceImpl cocktailService;

    @Test
    public void testInsertConcept() {

        CocktailConcept cocktail = new CocktailConcept();
        cocktail.setPreferredLabel("preferredLabel");
        cocktail.setAlternativeLabels(Collections.singletonList("alternativeLabel"));
        cocktail.setBroaderConcept("broaderConcept");

        //test
        cocktailService.insertConcept(cocktail);

        //verification
        verify(cocktailRepository).saveCocktail(Matchers.isA(Model.class));
    }

    @Test
    public void testGetAllConcepts() {

        Model model = prepareTestModel();
        when(cocktailRepository.getAllConcepts()).thenReturn(model);

        //test
        final String allConcepts = cocktailService.getAllConcepts();

        //verify
        assertTrue(allConcepts.contains("someLabel"));
        assertTrue(allConcepts.contains("Cocktail1"));
    }

    @Test
    public void testGetConcept() {
        Model model = prepareTestModel();
        when(cocktailRepository.getConcept(COCKTAIL_LABEL)).thenReturn(model);

        //test
        final String concept = cocktailService.getConcept(COCKTAIL_LABEL, "application/rdf+xml");

        //verify
        assertTrue(concept.contains("someLabel"));
        assertTrue(concept.contains("Cocktail1"));
    }

    @Test
    public void testDeleteConcept() {
        //prepare
        doNothing().when(cocktailRepository).deleteConcept(COCKTAIL_LABEL);

        //test
        cocktailService.deleteConcept(COCKTAIL_LABEL);

        //verify
        verify(cocktailRepository).deleteConcept(COCKTAIL_LABEL);
    }

    private Model prepareTestModel() {
        final ValueFactory factory = SimpleValueFactory.getInstance();
        final IRI cocktail1 = factory.createIRI("http://example.org/cocktail1#Cocktail1");
        final Literal prefLabel = factory.createLiteral("someLabel");
        final Statement statement = factory.createStatement(cocktail1, SKOS.PREF_LABEL, prefLabel);
        Model model = new TreeModel();
        model.add(statement);
        return model;
    }

}