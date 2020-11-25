package com.semantic.web.controller;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class EntryController {

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String hello() {
        return "{greeting: 'hello world'}";
    }

    @GetMapping(value = "/createConcept", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String createConcept() {
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

        return out.toString();
    }
}
