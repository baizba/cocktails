package com.semantic.web.service.impl;

import com.semantic.web.model.CocktailConcept;
import com.semantic.web.repository.CocktailRepository;
import com.semantic.web.service.CocktailService;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.stereotype.Service;

@Service("cocktailService")
public class CocktailServiceImpl implements CocktailService {

    private static final String COCKTAIL_NAMESPACE = "http://example.org/cocktail#";
    private static final String COCKTAIL_NAMESPACE_PREFIX = "ex";
    private static final String COCKTAIL_NAMESPACE_PREFIX_URL = COCKTAIL_NAMESPACE_PREFIX + ":";


    private final ModelBuilder builder;
    private final CocktailRepository cocktailRepository;

    public CocktailServiceImpl(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
        builder = new ModelBuilder();
        builder.setNamespace(COCKTAIL_NAMESPACE_PREFIX, COCKTAIL_NAMESPACE)
                .setNamespace(SKOS.NS)
                .setNamespace(RDF.NS);
    }

    @Override
    public void insertConcept(CocktailConcept cocktailConcept) {
        builder.subject(COCKTAIL_NAMESPACE_PREFIX_URL + cocktailConcept.getPreferredLabel())
                .add(RDF.TYPE, SKOS.CONCEPT)
                .add(SKOS.PREF_LABEL, cocktailConcept.getPreferredLabel());

        if (CollectionUtils.isNotEmpty(cocktailConcept.getAlternativeLabels())) {
            cocktailConcept.getAlternativeLabels().forEach(altLabel -> builder.add(SKOS.ALT_LABEL, altLabel));
        }

        Model model = builder.build();

        Rio.write(model, System.out, RDFFormat.RDFXML);

        cocktailRepository.saveCocktail(model);
    }
}
