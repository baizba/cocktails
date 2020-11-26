package com.semantic.web.model;

import java.util.List;

public class CocktailConcept {

    private String preferredLabel;
    private List<String> alternativeLabels;

    public String getPreferredLabel() {
        return preferredLabel;
    }

    public void setPreferredLabel(String preferredLabel) {
        this.preferredLabel = preferredLabel;
    }

    public List<String> getAlternativeLabels() {
        return alternativeLabels;
    }

    public void setAlternativeLabels(List<String> alternativeLabels) {
        this.alternativeLabels = alternativeLabels;
    }
}
