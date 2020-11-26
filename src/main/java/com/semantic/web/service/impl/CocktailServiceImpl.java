package com.semantic.web.service.impl;

import com.semantic.web.model.CocktailConcept;
import com.semantic.web.repository.CocktailRepository;
import com.semantic.web.service.CocktailService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        //add subject (cocktail), mark it as concept and set the preferred label
        builder.subject(COCKTAIL_NAMESPACE_PREFIX_URL + cocktailConcept.getPreferredLabel())
                .add(RDF.TYPE, SKOS.CONCEPT)
                .add(SKOS.PREF_LABEL, cocktailConcept.getPreferredLabel());

        //add alternative labels
        if (CollectionUtils.isNotEmpty(cocktailConcept.getAlternativeLabels())) {
            cocktailConcept.getAlternativeLabels().forEach(altLabel -> builder.add(SKOS.ALT_LABEL, altLabel));
        }

        //connection to broader concepts
        if (StringUtils.isNotBlank(cocktailConcept.getBroaderConcept())) {
            builder.add(SKOS.BROADER, cocktailConcept.getBroaderConcept());
        }

        Model model = builder.build();
        Rio.write(model, System.out, RDFFormat.RDFXML);

        cocktailRepository.saveCocktail(model);
    }
}
