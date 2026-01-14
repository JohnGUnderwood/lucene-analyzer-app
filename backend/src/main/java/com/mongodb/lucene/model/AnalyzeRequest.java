package com.mongodb.lucene.model;

public class AnalyzeRequest {
    private String indexText;
    private String queryText;
    private String indexAnalyzer;
    private String queryAnalyzer;
    private boolean useAutocomplete;
    private AutocompleteConfig autocompleteConfig;

    // Constructors
    public AnalyzeRequest() {
        this.autocompleteConfig = new AutocompleteConfig();
    }

    // Getters and Setters
    public String getIndexText() {
        return indexText;
    }

    public void setIndexText(String indexText) {
        this.indexText = indexText;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getIndexAnalyzer() {
        return indexAnalyzer;
    }

    public void setIndexAnalyzer(String indexAnalyzer) {
        this.indexAnalyzer = indexAnalyzer;
    }

    public String getQueryAnalyzer() {
        return queryAnalyzer;
    }

    public void setQueryAnalyzer(String queryAnalyzer) {
        this.queryAnalyzer = queryAnalyzer;
    }

    public boolean isUseAutocomplete() {
        return useAutocomplete;
    }

    public void setUseAutocomplete(boolean useAutocomplete) {
        this.useAutocomplete = useAutocomplete;
    }

    public AutocompleteConfig getAutocompleteConfig() {
        return autocompleteConfig;
    }

    public void setAutocompleteConfig(AutocompleteConfig autocompleteConfig) {
        this.autocompleteConfig = autocompleteConfig;
    }
}
