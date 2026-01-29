package com.mongodb.lucene.model.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CustomAnalyzerDefinition {
    @JsonProperty("name")
    private String name;

    @JsonProperty("charFilters")
    private List<CharFilterDefinition> charFilters;

    @JsonProperty("tokenizer")
    private TokenizerDefinition tokenizer;

    @JsonProperty("tokenFilters")
    private List<TokenFilterDefinition> tokenFilters;

    // Constructors
    public CustomAnalyzerDefinition() {}

    public CustomAnalyzerDefinition(String name, List<CharFilterDefinition> charFilters, 
                                   TokenizerDefinition tokenizer, List<TokenFilterDefinition> tokenFilters) {
        this.name = name;
        this.charFilters = charFilters;
        this.tokenizer = tokenizer;
        this.tokenFilters = tokenFilters;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CharFilterDefinition> getCharFilters() {
        return charFilters;
    }

    public void setCharFilters(List<CharFilterDefinition> charFilters) {
        this.charFilters = charFilters;
    }

    public TokenizerDefinition getTokenizer() {
        return tokenizer;
    }

    public void setTokenizer(TokenizerDefinition tokenizer) {
        this.tokenizer = tokenizer;
    }

    public List<TokenFilterDefinition> getTokenFilters() {
        return tokenFilters;
    }

    public void setTokenFilters(List<TokenFilterDefinition> tokenFilters) {
        this.tokenFilters = tokenFilters;
    }
}
