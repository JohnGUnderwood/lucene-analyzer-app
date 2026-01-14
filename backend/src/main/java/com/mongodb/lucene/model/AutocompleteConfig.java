package com.mongodb.lucene.model;

public class AutocompleteConfig {
    private String autocompleteType = "edgeGram"; // "edgeGram" or "nGram"
    private int minGrams = 3;
    private int maxGrams = 15;

    // Constructors
    public AutocompleteConfig() {}

    public AutocompleteConfig(String autocompleteType, int minGrams, int maxGrams) {
        this.autocompleteType = autocompleteType;
        this.minGrams = minGrams;
        this.maxGrams = maxGrams;
    }

    // Getters and Setters
    public String getAutocompleteType() {
        return autocompleteType;
    }

    public void setAutocompleteType(String autocompleteType) {
        this.autocompleteType = autocompleteType;
    }

    public int getMinGrams() {
        return minGrams;
    }

    public void setMinGrams(int minGrams) {
        this.minGrams = minGrams;
    }

    public int getMaxGrams() {
        return maxGrams;
    }

    public void setMaxGrams(int maxGrams) {
        this.maxGrams = maxGrams;
    }
}
