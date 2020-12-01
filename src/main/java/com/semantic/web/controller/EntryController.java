package com.semantic.web.controller;

import com.semantic.web.exception.ConceptSaveException;
import com.semantic.web.model.CocktailConcept;
import com.semantic.web.service.CocktailService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EntryController {

    private static final Logger LOG = LoggerFactory.getLogger(EntryController.class);
    private static final String ERROR_MESSAGE_SAVE = "error occurred, cocktail not added, check server log";
    private static final String PLEASE_SEND_VALID_ACCEPT_HEADER_IN_THE_REQUEST = "please send valid Accept header in the request";

    private final CocktailService cocktailService;

    public EntryController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @PutMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> createConcept(@RequestBody CocktailConcept cocktailConcept) {

        //validation
        if (cocktailConcept == null || StringUtils.isBlank(cocktailConcept.getPreferredLabel())) {
            return ResponseEntity.badRequest().build();
        }

        //insert to RDF
        try {
            cocktailService.insertConcept(cocktailConcept);
        } catch (ConceptSaveException e) {
            LOG.error(ERROR_MESSAGE_SAVE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_MESSAGE_SAVE);
        }

        return ResponseEntity.ok("ok");
    }

    @GetMapping(value = "/get")
    @ResponseBody
    public ResponseEntity<String> getAllConcepts() {
        final String conceptRdfXml = cocktailService.getAllConcepts();
        return ResponseEntity.ok(conceptRdfXml);

    }

    @GetMapping(value = "/get/{preferredLabel}")
    @ResponseBody
    public ResponseEntity<String> getConcept(@PathVariable("preferredLabel") String preferredLabel, @RequestHeader("Accept") String accept) {
        if (StringUtils.isBlank(accept)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PLEASE_SEND_VALID_ACCEPT_HEADER_IN_THE_REQUEST);
        }

        try {
            final String conceptRdfXml = cocktailService.getConcept(preferredLabel, accept);
            return ResponseEntity.ok(conceptRdfXml);
        } catch (UnsupportedRDFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PLEASE_SEND_VALID_ACCEPT_HEADER_IN_THE_REQUEST);
        }

    }
}
