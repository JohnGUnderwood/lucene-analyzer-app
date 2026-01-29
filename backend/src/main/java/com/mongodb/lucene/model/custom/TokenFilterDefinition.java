package com.mongodb.lucene.model.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import java.util.regex.Pattern;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TokenFilterDefinition.AsciiFoldingFilter.class, name = "asciiFolding"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.DaitchMokotoffSoundexFilter.class, name = "daitchMokotoffSoundex"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.EdgeGramFilter.class, name = "edgeGram"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.EnglishPossessiveFilter.class, name = "englishPossessive"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.FlattenGraphFilter.class, name = "flattenGraph"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.IcuFoldingFilter.class, name = "icuFolding"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.IcuNormalizerFilter.class, name = "icuNormalizer"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.KeywordRepeatFilter.class, name = "keywordRepeat"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.KStemFilter.class, name = "kStemming"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.LengthFilter.class, name = "length"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.LowercaseFilter.class, name = "lowercase"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.NGramFilter.class, name = "nGram"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.PorterStemmingFilter.class, name = "porterStemming"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.RegexFilter.class, name = "regex"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.RemoveDuplicatesFilter.class, name = "removeDuplicates"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.ReverseFilter.class, name = "reverse"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.ShingleFilter.class, name = "shingle"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.SnowballStemmingFilter.class, name = "snowballStemming"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.SpanishPluralStemmingFilter.class, name = "spanishPluralStemming"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.StempelFilter.class, name = "stempel"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.StopwordFilter.class, name = "stopword"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.TrimFilter.class, name = "trim"),
    @JsonSubTypes.Type(value = TokenFilterDefinition.WordDelimiterGraphFilter.class, name = "wordDelimiterGraph")
})
public abstract class TokenFilterDefinition {
    @JsonProperty("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // ASCII Folding Filter
    public static class AsciiFoldingFilter extends TokenFilterDefinition {
        @JsonProperty("preserveOriginal")
        private boolean preserveOriginal = false;

        public boolean isPreserveOriginal() {
            return preserveOriginal;
        }

        public void setPreserveOriginal(boolean preserveOriginal) {
            this.preserveOriginal = preserveOriginal;
        }
    }

    // Daitch Mokotoff Soundex Filter
    public static class DaitchMokotoffSoundexFilter extends TokenFilterDefinition {
        @JsonProperty("originalTokens")
        private String originalTokens = "omit"; // "include" or "omit"

        public String getOriginalTokens() {
            return originalTokens;
        }

        public void setOriginalTokens(String originalTokens) {
            this.originalTokens = originalTokens;
        }
    }

    // Edge Gram Filter
    public static class EdgeGramFilter extends TokenFilterDefinition {
        @JsonProperty("minGram")
        private int minGram;

        @JsonProperty("maxGram")
        private int maxGram;

        @JsonProperty("termNotInBounds")
        private String termNotInBounds = "omit"; // "include" or "omit"

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

        public String getTermNotInBounds() {
            return termNotInBounds;
        }

        public void setTermNotInBounds(String termNotInBounds) {
            this.termNotInBounds = termNotInBounds;
        }
    }

    // English Possessive Filter
    public static class EnglishPossessiveFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // Flatten Graph Filter
    public static class FlattenGraphFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // ICU Folding Filter
    public static class IcuFoldingFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // ICU Normalizer Filter
    public static class IcuNormalizerFilter extends TokenFilterDefinition {
        @JsonProperty("normalizationForm")
        private String normalizationForm = "nfc"; // nfc, nfd, nfkc, nfkd

        public String getNormalizationForm() {
            return normalizationForm;
        }

        public void setNormalizationForm(String normalizationForm) {
            this.normalizationForm = normalizationForm;
        }
    }

    // Keyword Repeat Filter
    public static class KeywordRepeatFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // K-Stemming Filter
    public static class KStemFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // Length Filter
    public static class LengthFilter extends TokenFilterDefinition {
        @JsonProperty("min")
        private int min;

        @JsonProperty("max")
        private int max;

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }
    }

    // Lowercase Filter
    public static class LowercaseFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // N-Gram Filter
    public static class NGramFilter extends TokenFilterDefinition {
        @JsonProperty("minGram")
        private int minGram;

        @JsonProperty("maxGram")
        private int maxGram;

        @JsonProperty("termNotInBounds")
        private String termNotInBounds = "omit"; // "include" or "omit"

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

        public String getTermNotInBounds() {
            return termNotInBounds;
        }

        public void setTermNotInBounds(String termNotInBounds) {
            this.termNotInBounds = termNotInBounds;
        }
    }

    // Porter Stemming Filter
    public static class PorterStemmingFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // Regex Filter
    public static class RegexFilter extends TokenFilterDefinition {
        @JsonProperty("pattern")
        private String pattern;

        @JsonProperty("replacement")
        private String replacement;

        @JsonProperty("matches")
        private String matches = "all"; // "all" or "first"

        private transient Pattern compiledPattern;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
            this.compiledPattern = null;
        }

        public Pattern getCompiledPattern() {
            if (compiledPattern == null && pattern != null) {
                compiledPattern = Pattern.compile(pattern);
            }
            return compiledPattern;
        }

        public String getReplacement() {
            return replacement;
        }

        public void setReplacement(String replacement) {
            this.replacement = replacement;
        }

        public String getMatches() {
            return matches;
        }

        public void setMatches(String matches) {
            this.matches = matches;
        }
    }

    // Remove Duplicates Filter
    public static class RemoveDuplicatesFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // Reverse Filter
    public static class ReverseFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // Shingle Filter
    public static class ShingleFilter extends TokenFilterDefinition {
        @JsonProperty("minShingleSize")
        private int minShingleSize = 2;

        @JsonProperty("maxShingleSize")
        private int maxShingleSize = 2;

        public int getMinShingleSize() {
            return minShingleSize;
        }

        public void setMinShingleSize(int minShingleSize) {
            this.minShingleSize = minShingleSize;
        }

        public int getMaxShingleSize() {
            return maxShingleSize;
        }

        public void setMaxShingleSize(int maxShingleSize) {
            this.maxShingleSize = maxShingleSize;
        }
    }

    // Snowball Stemming Filter
    public static class SnowballStemmingFilter extends TokenFilterDefinition {
        @JsonProperty("stemmerName")
        private String stemmerName; // Arabic, Armenian, Basque, etc.

        public String getStemmerName() {
            return stemmerName;
        }

        public void setStemmerName(String stemmerName) {
            this.stemmerName = stemmerName;
        }
    }

    // Spanish Plural Stemming Filter
    public static class SpanishPluralStemmingFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // Stempel Filter
    public static class StempelFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // Stopword Filter
    public static class StopwordFilter extends TokenFilterDefinition {
        @JsonProperty("tokens")
        private List<String> tokens;

        @JsonProperty("ignoreCase")
        private boolean ignoreCase = true;

        public List<String> getTokens() {
            return tokens;
        }

        public void setTokens(List<String> tokens) {
            this.tokens = tokens;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public void setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
        }
    }

    // Trim Filter
    public static class TrimFilter extends TokenFilterDefinition {
        // No additional properties
    }

    // Word Delimiter Graph Filter
    public static class WordDelimiterGraphFilter extends TokenFilterDefinition {
        @JsonProperty("generateWordParts")
        private boolean generateWordParts = true;

        @JsonProperty("generateNumberParts")
        private boolean generateNumberParts = true;

        @JsonProperty("catenateWords")
        private boolean catenateWords = false;

        @JsonProperty("catenateNumbers")
        private boolean catenateNumbers = false;

        @JsonProperty("catenateAll")
        private boolean catenateAll = false;

        @JsonProperty("splitOnCaseChange")
        private boolean splitOnCaseChange = true;

        @JsonProperty("preserveOriginal")
        private boolean preserveOriginal = false;

        @JsonProperty("splitOnNumerics")
        private boolean splitOnNumerics = true;

        @JsonProperty("stemEnglishPossessive")
        private boolean stemEnglishPossessive = true;

        @JsonProperty("protectedWords")
        private List<String> protectedWords;

        @JsonProperty("ignoreCase")
        private boolean ignoreCase = false;

        // Getters and Setters
        public boolean isGenerateWordParts() {
            return generateWordParts;
        }

        public void setGenerateWordParts(boolean generateWordParts) {
            this.generateWordParts = generateWordParts;
        }

        public boolean isGenerateNumberParts() {
            return generateNumberParts;
        }

        public void setGenerateNumberParts(boolean generateNumberParts) {
            this.generateNumberParts = generateNumberParts;
        }

        public boolean isCatenateWords() {
            return catenateWords;
        }

        public void setCatenateWords(boolean catenateWords) {
            this.catenateWords = catenateWords;
        }

        public boolean isCatenateNumbers() {
            return catenateNumbers;
        }

        public void setCatenateNumbers(boolean catenateNumbers) {
            this.catenateNumbers = catenateNumbers;
        }

        public boolean isCatenateAll() {
            return catenateAll;
        }

        public void setCatenateAll(boolean catenateAll) {
            this.catenateAll = catenateAll;
        }

        public boolean isSplitOnCaseChange() {
            return splitOnCaseChange;
        }

        public void setSplitOnCaseChange(boolean splitOnCaseChange) {
            this.splitOnCaseChange = splitOnCaseChange;
        }

        public boolean isPreserveOriginal() {
            return preserveOriginal;
        }

        public void setPreserveOriginal(boolean preserveOriginal) {
            this.preserveOriginal = preserveOriginal;
        }

        public boolean isSplitOnNumerics() {
            return splitOnNumerics;
        }

        public void setSplitOnNumerics(boolean splitOnNumerics) {
            this.splitOnNumerics = splitOnNumerics;
        }

        public boolean isStemEnglishPossessive() {
            return stemEnglishPossessive;
        }

        public void setStemEnglishPossessive(boolean stemEnglishPossessive) {
            this.stemEnglishPossessive = stemEnglishPossessive;
        }

        public List<String> getProtectedWords() {
            return protectedWords;
        }

        public void setProtectedWords(List<String> protectedWords) {
            this.protectedWords = protectedWords;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public void setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
        }
    }
}
