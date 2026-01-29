package com.mongodb.lucene.model.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.regex.Pattern;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TokenizerDefinition.EdgeGramTokenizer.class, name = "edgeGram"),
    @JsonSubTypes.Type(value = TokenizerDefinition.KeywordTokenizer.class, name = "keyword"),
    @JsonSubTypes.Type(value = TokenizerDefinition.NGramTokenizer.class, name = "nGram"),
    @JsonSubTypes.Type(value = TokenizerDefinition.RegexCaptureGroupTokenizer.class, name = "regexCaptureGroup"),
    @JsonSubTypes.Type(value = TokenizerDefinition.RegexSplitTokenizer.class, name = "regexSplit"),
    @JsonSubTypes.Type(value = TokenizerDefinition.StandardTokenizer.class, name = "standard"),
    @JsonSubTypes.Type(value = TokenizerDefinition.UaxUrlEmailTokenizer.class, name = "uaxUrlEmail"),
    @JsonSubTypes.Type(value = TokenizerDefinition.WhitespaceTokenizer.class, name = "whitespace")
})
public abstract class TokenizerDefinition {
    @JsonProperty("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Edge Gram Tokenizer
    public static class EdgeGramTokenizer extends TokenizerDefinition {
        @JsonProperty("minGram")
        private int minGram;

        @JsonProperty("maxGram")
        private int maxGram;

        public int getMinGram() {
            return minGram;
        }

        public void setMinGram(int minGram) {
            this.minGram = minGram;
        }

        public int getMaxGram() {
            return maxGram;
        }

        public void setMaxGram(int maxGram) {
            this.maxGram = maxGram;
        }
    }

    // Keyword Tokenizer
    public static class KeywordTokenizer extends TokenizerDefinition {
        // No additional properties
    }

    // N-Gram Tokenizer
    public static class NGramTokenizer extends TokenizerDefinition {
        @JsonProperty("minGram")
        private int minGram;

        @JsonProperty("maxGram")
        private int maxGram;

        public int getMinGram() {
            return minGram;
        }

        public void setMinGram(int minGram) {
            this.minGram = minGram;
        }

        public int getMaxGram() {
            return maxGram;
        }

        public void setMaxGram(int maxGram) {
            this.maxGram = maxGram;
        }
    }

    // Regex Capture Group Tokenizer
    public static class RegexCaptureGroupTokenizer extends TokenizerDefinition {
        @JsonProperty("pattern")
        private String pattern;

        @JsonProperty("group")
        private int group;

        private transient Pattern compiledPattern;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
            this.compiledPattern = null; // Reset compiled pattern
        }

        public Pattern getCompiledPattern() {
            if (compiledPattern == null && pattern != null) {
                compiledPattern = Pattern.compile(pattern);
            }
            return compiledPattern;
        }

        public int getGroup() {
            return group;
        }

        public void setGroup(int group) {
            this.group = group;
        }
    }

    // Regex Split Tokenizer
    public static class RegexSplitTokenizer extends TokenizerDefinition {
        @JsonProperty("pattern")
        private String pattern;

        private transient Pattern compiledPattern;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
            this.compiledPattern = null; // Reset compiled pattern
        }

        public Pattern getCompiledPattern() {
            if (compiledPattern == null && pattern != null) {
                compiledPattern = Pattern.compile(pattern);
            }
            return compiledPattern;
        }
    }

    // Standard Tokenizer
    public static class StandardTokenizer extends TokenizerDefinition {
        @JsonProperty("maxTokenLength")
        private Integer maxTokenLength;

        public Integer getMaxTokenLength() {
            return maxTokenLength;
        }

        public void setMaxTokenLength(Integer maxTokenLength) {
            this.maxTokenLength = maxTokenLength;
        }
    }

    // UAX URL Email Tokenizer
    public static class UaxUrlEmailTokenizer extends TokenizerDefinition {
        @JsonProperty("maxTokenLength")
        private Integer maxTokenLength;

        public Integer getMaxTokenLength() {
            return maxTokenLength;
        }

        public void setMaxTokenLength(Integer maxTokenLength) {
            this.maxTokenLength = maxTokenLength;
        }
    }

    // Whitespace Tokenizer
    public static class WhitespaceTokenizer extends TokenizerDefinition {
        @JsonProperty("maxTokenLength")
        private Integer maxTokenLength;

        public Integer getMaxTokenLength() {
            return maxTokenLength;
        }

        public void setMaxTokenLength(Integer maxTokenLength) {
            this.maxTokenLength = maxTokenLength;
        }
    }
}
