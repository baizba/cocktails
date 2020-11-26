package com.semantic.web.controller;

import com.semantic.web.model.CocktailConcept;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class EntryController {

    @PutMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createConcept(@RequestBody CocktailConcept cocktailConcept) {
        // set some namespaces
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("ex", "http://example.org/cocktail#")
                .setNamespace(SKOS.NS)
                .setNamespace(RDF.NS);


        //validation
        if (cocktailConcept == null || StringUtils.isBlank(cocktailConcept.getPreferredLabel())) {
            return ResponseEntity.badRequest().build();
        }

        // add a new named graph to the model
        builder.subject("ex:" + cocktailConcept.getPreferredLabel())
                .add(RDF.TYPE, SKOS.CONCEPT)
                .add(SKOS.PREF_LABEL, cocktailConcept.getPreferredLabel());

        if (CollectionUtils.isNotEmpty(cocktailConcept.getAlternativeLabels())) {
            cocktailConcept.getAlternativeLabels().forEach(altLabel -> builder.add(SKOS.ALT_LABEL, altLabel));
        }

        // add a triple to the default graph
        //builder.defaultGraph().subject("ex:graph1").add(RDF.TYPE, "ex:Graph");

        // return the Model object
        Model model = builder.build();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Rio.write(model, out, RDFFormat.RDFXML);

        return ResponseEntity.ok(out.toString());
    }
}
