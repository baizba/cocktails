package com.semantic.web.controller;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class EntryController {

    @PutMapping(value = "/create", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createConcept() {
        // set some namespaces
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("ex", "http://example.org/cocktail#")
                .setNamespace(SKOS.NS)
                .setNamespace(RDF.NS);

        // add a new named graph to the model
        builder.subject("ex:Mimosa")
                .add(RDF.TYPE, SKOS.CONCEPT)
                .add(SKOS.PREF_LABEL, "Mimosa")
                .add(SKOS.ALT_LABEL, "Mimi");

        // add a triple to the default graph
        //builder.defaultGraph().subject("ex:graph1").add(RDF.TYPE, "ex:Graph");

        // return the Model object
        Model model = builder.build();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Rio.write(model, out, RDFFormat.RDFXML);

        return ResponseEntity.ok(out.toString());
    }
}
