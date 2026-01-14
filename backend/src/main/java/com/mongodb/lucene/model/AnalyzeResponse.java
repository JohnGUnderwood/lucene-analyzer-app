package com.mongodb.lucene.model;

import java.util.List;
import java.util.Set;

public class AnalyzeResponse {
    private List<TokenInfo> indexTokens;
    private List<TokenInfo> queryTokens;
    private Set<String> matchingTokens;
    private String analyzerUsed;

    // Constructors
    public AnalyzeResponse() {}

    public AnalyzeResponse(List<TokenInfo> indexTokens, List<TokenInfo> queryTokens, 
                          Set<String> matchingTokens, String analyzerUsed) {
        this.indexTokens = indexTokens;
        this.queryTokens = queryTokens;
        this.matchingTokens = matchingTokens;
        this.analyzerUsed = analyzerUsed;
    }

    // Getters and Setters
    public List<TokenInfo> getIndexTokens() {
        return indexTokens;
    }

    public void setIndexTokens(List<TokenInfo> indexTokens) {
        this.indexTokens = indexTokens;
    }

    public List<TokenInfo> getQueryTokens() {
        return queryTokens;
    }

    public void setQueryTokens(List<TokenInfo> queryTokens) {
        this.queryTokens = queryTokens;
    }

    public Set<String> getMatchingTokens() {
        return matchingTokens;
    }

    public void setMatchingTokens(Set<String> matchingTokens) {
        this.matchingTokens = matchingTokens;
    }

    public String getAnalyzerUsed() {
        return analyzerUsed;
    }

    public void setAnalyzerUsed(String analyzerUsed) {
        this.analyzerUsed = analyzerUsed;
    }
}
