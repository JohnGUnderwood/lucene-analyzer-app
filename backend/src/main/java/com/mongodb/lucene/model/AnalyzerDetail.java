package com.mongodb.lucene.model;

public class AnalyzerDetail {
    private String name;
    private String category;
    private boolean disabled;
    private String additionalLabel;

    // Constructors
    public AnalyzerDetail() {}

    public AnalyzerDetail(String name, String category) {
        this.name = name;
        this.category = category;
        this.disabled = false;
        this.additionalLabel = "";
    }

    public AnalyzerDetail(String name, String category, boolean disabled, String additionalLabel) {
        this.name = name;
        this.category = category;
        this.disabled = disabled;
        this.additionalLabel = additionalLabel;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getAdditionalLabel() {
        return additionalLabel;
    }

    public void setAdditionalLabel(String additionalLabel) {
        this.additionalLabel = additionalLabel;
    }
}
