package com.semantic.web.service.impl;

import com.semantic.web.model.CocktailConcept;
import com.semantic.web.repository.CocktailRepository;
import com.semantic.web.service.CocktailService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service("cocktailService")
public class CocktailServiceImpl implements CocktailService {

    private static final String COCKTAIL_NAMESPACE_PREFIX = "ex";
    private static final String COCKTAIL_NAMESPACE_URL = "http://example.org/cocktail#";
    private static final String COCKTAIL_NAMESPACE_PREFIX_URL = COCKTAIL_NAMESPACE_PREFIX + ":";
    private static final Namespace COCKTAIL_NAMESPACE = new SimpleNamespace(COCKTAIL_NAMESPACE_PREFIX, COCKTAIL_NAMESPACE_URL);
    private static final String RDF_XML = "application/rdf+xml";
    private static final String RDF_JSON = "application/rdf+json";

    private final CocktailRepository cocktailRepository;

    public CocktailServiceImpl(final CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }

    @Override
    public void insertConcept(final CocktailConcept cocktailConcept) {
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace(COCKTAIL_NAMESPACE).setNamespace(SKOS.NS).setNamespace(RDF.NS);

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

        cocktailRepository.saveCocktail(model);
    }

    @Override
    public String getAllConcepts() {
        final Model model = cocktailRepository.getAllConcepts();
        if (CollectionUtils.isEmpty(model)) {
            return StringUtils.EMPTY;
        }
        return convertRDFModelToString(RDFFormat.RDFXML, model);
    }

    @Override
    public String getConcept(final String preferredLabel, final String accept) {
        final RDFFormat rdfFormat = parseRDFFormat(accept);
        final Model model = cocktailRepository.getConcept(preferredLabel);
        if (CollectionUtils.isEmpty(model)) {
            return StringUtils.EMPTY;
        }
        return convertRDFModelToString(rdfFormat, model);
    }

    private String convertRDFModelToString(RDFFormat rdfFormat, Model model) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Rio.write(model, out, rdfFormat);
        return out.toString();
    }

    private RDFFormat parseRDFFormat(final String rdfFormatAsString) {
        if (RDF_XML.equals(rdfFormatAsString)) {
            return RDFFormat.RDFXML;
        } else if (RDF_JSON.equals(rdfFormatAsString)) {
            return RDFFormat.RDFJSON;
        } else {
            throw new UnsupportedRDFormatException("this format of RDF is not supported: " + rdfFormatAsString);
        }
    }
}
