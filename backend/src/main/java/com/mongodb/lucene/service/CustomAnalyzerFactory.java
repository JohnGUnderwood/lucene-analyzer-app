package com.mongodb.lucene.service;

import com.mongodb.lucene.model.custom.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.charfilter.MappingCharFilter;
import org.apache.lucene.analysis.charfilter.NormalizeCharMap;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.email.UAX29URLEmailTokenizer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.es.SpanishPluralStemFilter;
import org.apache.lucene.analysis.fa.PersianCharFilter;
import org.apache.lucene.analysis.icu.ICUFoldingFilter;
import org.apache.lucene.analysis.icu.ICUNormalizer2CharFilter;
import org.apache.lucene.analysis.icu.ICUNormalizer2Filter;
import org.apache.lucene.analysis.miscellaneous.*;
import org.apache.lucene.analysis.ngram.*;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.phonetic.DaitchMokotoffSoundexFilter;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.analysis.reverse.ReverseStringFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.stempel.StempelFilter;
import org.apache.lucene.analysis.stempel.StempelStemmer;
import org.tartarus.snowball.ext.*;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Factory for creating custom Lucene analyzers from analyzer definitions.
 * Supports all 37 components: 4 char filters, 8 tokenizers, and 25 token filters.
 */
public class CustomAnalyzerFactory {

    /**
     * Build a custom analyzer from a definition.
     */
    public static Analyzer build(CustomAnalyzerDefinition definition) {
        if (definition.getTokenizer() == null) {
            throw new IllegalArgumentException("Tokenizer is required");
        }

        List<Function<Reader, Reader>> charFilters = buildCharFilters(
            definition.getCharFilters() != null ? definition.getCharFilters() : Collections.emptyList()
        );
        
        Function<String, Tokenizer> tokenizerFactory = buildTokenizer(definition.getTokenizer());
        
        List<Function<TokenStream, TokenStream>> tokenFilters = buildTokenFilters(
            definition.getTokenFilters() != null ? definition.getTokenFilters() : Collections.emptyList()
        );

        return new CustomAnalyzer(charFilters, tokenizerFactory, tokenFilters);
    }

    /**
     * Build character filters.
     */
    private static List<Function<Reader, Reader>> buildCharFilters(List<CharFilterDefinition> definitions) {
        List<Function<Reader, Reader>> filters = new ArrayList<>();
        for (CharFilterDefinition def : definitions) {
            filters.add(buildCharFilter(def));
        }
        return filters;
    }

    /**
     * Build a single character filter.
     */
    private static Function<Reader, Reader> buildCharFilter(CharFilterDefinition def) {
        if (def instanceof CharFilterDefinition.HtmlStripCharFilter) {
            CharFilterDefinition.HtmlStripCharFilter filter = (CharFilterDefinition.HtmlStripCharFilter) def;
            Set<String> ignoredTags = filter.getIgnoredTags();
            return reader -> new HTMLStripCharFilter(reader, ignoredTags);
        } else if (def instanceof CharFilterDefinition.IcuNormalizeCharFilter) {
            return reader -> new ICUNormalizer2CharFilter(reader);
        } else if (def instanceof CharFilterDefinition.MappingCharFilter) {
            CharFilterDefinition.MappingCharFilter filter = (CharFilterDefinition.MappingCharFilter) def;
            NormalizeCharMap.Builder builder = new NormalizeCharMap.Builder();
            if (filter.getMappings() != null) {
                filter.getMappings().forEach(builder::add);
            }
            NormalizeCharMap charMap = builder.build();
            return reader -> new MappingCharFilter(charMap, reader);
        } else if (def instanceof CharFilterDefinition.PersianCharFilter) {
            return reader -> new PersianCharFilter(reader);
        }
        throw new IllegalArgumentException("Unknown char filter type: " + def.getType());
    }

    /**
     * Build tokenizer.
     */
    private static Function<String, Tokenizer> buildTokenizer(TokenizerDefinition def) {
        if (def instanceof TokenizerDefinition.EdgeGramTokenizer) {
            TokenizerDefinition.EdgeGramTokenizer tokenizer = (TokenizerDefinition.EdgeGramTokenizer) def;
            return fieldName -> new EdgeNGramTokenizer(tokenizer.getMinGram(), tokenizer.getMaxGram());
        } else if (def instanceof TokenizerDefinition.KeywordTokenizer) {
            return fieldName -> new KeywordTokenizer();
        } else if (def instanceof TokenizerDefinition.NGramTokenizer) {
            TokenizerDefinition.NGramTokenizer tokenizer = (TokenizerDefinition.NGramTokenizer) def;
            return fieldName -> new NGramTokenizer(tokenizer.getMinGram(), tokenizer.getMaxGram());
        } else if (def instanceof TokenizerDefinition.RegexCaptureGroupTokenizer) {
            TokenizerDefinition.RegexCaptureGroupTokenizer tokenizer = (TokenizerDefinition.RegexCaptureGroupTokenizer) def;
            return fieldName -> new PatternTokenizer(tokenizer.getCompiledPattern(), tokenizer.getGroup());
        } else if (def instanceof TokenizerDefinition.RegexSplitTokenizer) {
            TokenizerDefinition.RegexSplitTokenizer tokenizer = (TokenizerDefinition.RegexSplitTokenizer) def;
            return fieldName -> new PatternTokenizer(tokenizer.getCompiledPattern(), -1);
        } else if (def instanceof TokenizerDefinition.StandardTokenizer) {
            TokenizerDefinition.StandardTokenizer tokenizer = (TokenizerDefinition.StandardTokenizer) def;
            return fieldName -> {
                StandardTokenizer st = new StandardTokenizer();
                if (tokenizer.getMaxTokenLength() != null) {
                    st.setMaxTokenLength(tokenizer.getMaxTokenLength());
                }
                return st;
            };
        } else if (def instanceof TokenizerDefinition.UaxUrlEmailTokenizer) {
            TokenizerDefinition.UaxUrlEmailTokenizer tokenizer = (TokenizerDefinition.UaxUrlEmailTokenizer) def;
            return fieldName -> {
                UAX29URLEmailTokenizer uax = new UAX29URLEmailTokenizer();
                if (tokenizer.getMaxTokenLength() != null) {
                    uax.setMaxTokenLength(tokenizer.getMaxTokenLength());
                }
                return uax;
            };
        } else if (def instanceof TokenizerDefinition.WhitespaceTokenizer) {
            TokenizerDefinition.WhitespaceTokenizer tokenizer = (TokenizerDefinition.WhitespaceTokenizer) def;
            return fieldName -> {
                if (tokenizer.getMaxTokenLength() != null) {
                    return new WhitespaceTokenizer(tokenizer.getMaxTokenLength());
                }
                return new WhitespaceTokenizer();
            };
        }
        throw new IllegalArgumentException("Unknown tokenizer type: " + def.getType());
    }

    /**
     * Build token filters.
     */
    private static List<Function<TokenStream, TokenStream>> buildTokenFilters(List<TokenFilterDefinition> definitions) {
        List<Function<TokenStream, TokenStream>> filters = new ArrayList<>();
        for (TokenFilterDefinition def : definitions) {
            filters.add(buildTokenFilter(def));
        }
        return filters;
    }

    /**
     * Build a single token filter.
     */
    private static Function<TokenStream, TokenStream> buildTokenFilter(TokenFilterDefinition def) {
        if (def instanceof TokenFilterDefinition.AsciiFoldingFilter) {
            TokenFilterDefinition.AsciiFoldingFilter filter = (TokenFilterDefinition.AsciiFoldingFilter) def;
            return input -> new ASCIIFoldingFilter(input, filter.isPreserveOriginal());
        } else if (def instanceof TokenFilterDefinition.DaitchMokotoffSoundexFilter) {
            TokenFilterDefinition.DaitchMokotoffSoundexFilter filter = (TokenFilterDefinition.DaitchMokotoffSoundexFilter) def;
            boolean includeOriginal = "include".equalsIgnoreCase(filter.getOriginalTokens());
            return input -> new DaitchMokotoffSoundexFilter(input, includeOriginal);
        } else if (def instanceof TokenFilterDefinition.EdgeGramFilter) {
            TokenFilterDefinition.EdgeGramFilter filter = (TokenFilterDefinition.EdgeGramFilter) def;
            boolean preserveOriginal = "include".equalsIgnoreCase(filter.getTermNotInBounds());
            return input -> new EdgeNGramTokenFilter(input, filter.getMinGram(), filter.getMaxGram(), preserveOriginal);
        } else if (def instanceof TokenFilterDefinition.EnglishPossessiveFilter) {
            return input -> new EnglishPossessiveFilter(input);
        } else if (def instanceof TokenFilterDefinition.FlattenGraphFilter) {
            return input -> new FlattenGraphFilter(input);
        } else if (def instanceof TokenFilterDefinition.IcuFoldingFilter) {
            return input -> new ICUFoldingFilter(input);
        } else if (def instanceof TokenFilterDefinition.IcuNormalizerFilter) {
            TokenFilterDefinition.IcuNormalizerFilter filter = (TokenFilterDefinition.IcuNormalizerFilter) def;
            return input -> {
                com.ibm.icu.text.Normalizer2 normalizer;
                switch (filter.getNormalizationForm().toLowerCase()) {
                    case "nfd":
                        normalizer = com.ibm.icu.text.Normalizer2.getNFDInstance();
                        break;
                    case "nfkc":
                        normalizer = com.ibm.icu.text.Normalizer2.getNFKCInstance();
                        break;
                    case "nfkd":
                        normalizer = com.ibm.icu.text.Normalizer2.getNFKDInstance();
                        break;
                    case "nfc":
                    default:
                        normalizer = com.ibm.icu.text.Normalizer2.getNFCInstance();
                        break;
                }
                return new ICUNormalizer2Filter(input, normalizer);
            };
        } else if (def instanceof TokenFilterDefinition.KeywordRepeatFilter) {
            return input -> new KeywordRepeatFilter(input);
        } else if (def instanceof TokenFilterDefinition.KStemFilter) {
            return input -> new KStemFilter(input);
        } else if (def instanceof TokenFilterDefinition.LengthFilter) {
            TokenFilterDefinition.LengthFilter filter = (TokenFilterDefinition.LengthFilter) def;
            return input -> new LengthFilter(input, filter.getMin(), filter.getMax());
        } else if (def instanceof TokenFilterDefinition.LowercaseFilter) {
            return input -> new LowerCaseFilter(input);
        } else if (def instanceof TokenFilterDefinition.NGramFilter) {
            TokenFilterDefinition.NGramFilter filter = (TokenFilterDefinition.NGramFilter) def;
            boolean preserveOriginal = "include".equalsIgnoreCase(filter.getTermNotInBounds());
            return input -> new NGramTokenFilter(input, filter.getMinGram(), filter.getMaxGram(), preserveOriginal);
        } else if (def instanceof TokenFilterDefinition.PorterStemmingFilter) {
            return input -> new PorterStemFilter(input);
        } else if (def instanceof TokenFilterDefinition.RegexFilter) {
            TokenFilterDefinition.RegexFilter filter = (TokenFilterDefinition.RegexFilter) def;
            boolean replaceAll = "all".equalsIgnoreCase(filter.getMatches());
            return input -> new PatternReplaceFilter(input, filter.getCompiledPattern(), filter.getReplacement(), replaceAll);
        } else if (def instanceof TokenFilterDefinition.RemoveDuplicatesFilter) {
            return input -> new RemoveDuplicatesTokenFilter(input);
        } else if (def instanceof TokenFilterDefinition.ReverseFilter) {
            return input -> new ReverseStringFilter(input);
        } else if (def instanceof TokenFilterDefinition.ShingleFilter) {
            TokenFilterDefinition.ShingleFilter filter = (TokenFilterDefinition.ShingleFilter) def;
            return input -> {
                ShingleFilter shingle = new ShingleFilter(input, filter.getMinShingleSize(), filter.getMaxShingleSize());
                shingle.setOutputUnigrams(false);
                return shingle;
            };
        } else if (def instanceof TokenFilterDefinition.SnowballStemmingFilter) {
            TokenFilterDefinition.SnowballStemmingFilter filter = (TokenFilterDefinition.SnowballStemmingFilter) def;
            return input -> {
                org.tartarus.snowball.SnowballStemmer stemmer = getSnowballStemmer(filter.getStemmerName());
                return new SnowballFilter(input, stemmer);
            };
        } else if (def instanceof TokenFilterDefinition.SpanishPluralStemmingFilter) {
            return input -> new SpanishPluralStemFilter(input);
        } else if (def instanceof TokenFilterDefinition.StempelFilter) {
            return input -> new StempelFilter(input, new StempelStemmer(PolishAnalyzer.getDefaultTable()));
        } else if (def instanceof TokenFilterDefinition.StopwordFilter) {
            TokenFilterDefinition.StopwordFilter filter = (TokenFilterDefinition.StopwordFilter) def;
            CharArraySet stopSet = StopFilter.makeStopSet(
                filter.getTokens().toArray(new String[0]), 
                filter.isIgnoreCase()
            );
            return input -> new StopFilter(input, stopSet);
        } else if (def instanceof TokenFilterDefinition.TrimFilter) {
            return input -> new TrimFilter(input);
        } else if (def instanceof TokenFilterDefinition.WordDelimiterGraphFilter) {
            TokenFilterDefinition.WordDelimiterGraphFilter filter = (TokenFilterDefinition.WordDelimiterGraphFilter) def;
            int flags = 0;
            if (filter.isGenerateWordParts()) flags |= WordDelimiterGraphFilter.GENERATE_WORD_PARTS;
            if (filter.isGenerateNumberParts()) flags |= WordDelimiterGraphFilter.GENERATE_NUMBER_PARTS;
            if (filter.isCatenateWords()) flags |= WordDelimiterGraphFilter.CATENATE_WORDS;
            if (filter.isCatenateNumbers()) flags |= WordDelimiterGraphFilter.CATENATE_NUMBERS;
            if (filter.isCatenateAll()) flags |= WordDelimiterGraphFilter.CATENATE_ALL;
            if (filter.isSplitOnCaseChange()) flags |= WordDelimiterGraphFilter.SPLIT_ON_CASE_CHANGE;
            if (filter.isPreserveOriginal()) flags |= WordDelimiterGraphFilter.PRESERVE_ORIGINAL;
            if (filter.isSplitOnNumerics()) flags |= WordDelimiterGraphFilter.SPLIT_ON_NUMERICS;
            if (filter.isStemEnglishPossessive()) flags |= WordDelimiterGraphFilter.STEM_ENGLISH_POSSESSIVE;
            
            CharArraySet protectedWords = filter.getProtectedWords() != null 
                ? new CharArraySet(filter.getProtectedWords(), filter.isIgnoreCase())
                : new CharArraySet(0, false);
            
            int finalFlags = flags;
            return input -> new WordDelimiterGraphFilter(input, finalFlags, protectedWords);
        }
        throw new IllegalArgumentException("Unknown token filter type: " + def.getType());
    }

    /**
     * Get snowball stemmer by name.
     */
    private static org.tartarus.snowball.SnowballStemmer getSnowballStemmer(String stemmerName) {
        return switch (stemmerName.toLowerCase()) {
            case "arabic" -> new ArabicStemmer();
            case "armenian" -> new ArmenianStemmer();
            case "basque" -> new BasqueStemmer();
            case "catalan" -> new CatalanStemmer();
            case "danish" -> new DanishStemmer();
            case "dutch" -> new DutchStemmer();
            case "english" -> new EnglishStemmer();
            case "estonian" -> new EstonianStemmer();
            case "finnish" -> new FinnishStemmer();
            case "french" -> new FrenchStemmer();
            case "german" -> new GermanStemmer();
            case "german2" -> new German2Stemmer();
            case "greek" -> new GreekStemmer();
            case "hindi" -> new HindiStemmer();
            case "hungarian" -> new HungarianStemmer();
            case "indonesian" -> new IndonesianStemmer();
            case "irish" -> new IrishStemmer();
            case "italian" -> new ItalianStemmer();
            case "lithuanian" -> new LithuanianStemmer();
            case "nepali" -> new NepaliStemmer();
            case "norwegian" -> new NorwegianStemmer();
            case "porter" -> new PorterStemmer();
            case "portuguese" -> new PortugueseStemmer();
            case "romanian" -> new RomanianStemmer();
            case "russian" -> new RussianStemmer();
            case "serbian" -> new SerbianStemmer();
            case "spanish" -> new SpanishStemmer();
            case "swedish" -> new SwedishStemmer();
            case "tamil" -> new TamilStemmer();
            case "turkish" -> new TurkishStemmer();
            case "yiddish" -> new YiddishStemmer();
            default -> throw new IllegalArgumentException("Unknown snowball stemmer: " + stemmerName);
        };
    }

    /**
     * Custom Analyzer implementation that chains components together.
     */
    private static class CustomAnalyzer extends Analyzer {
        private final List<Function<Reader, Reader>> charFilters;
        private final Function<String, Tokenizer> tokenizerFactory;
        private final List<Function<TokenStream, TokenStream>> tokenFilters;

        public CustomAnalyzer(
            List<Function<Reader, Reader>> charFilters,
            Function<String, Tokenizer> tokenizerFactory,
            List<Function<TokenStream, TokenStream>> tokenFilters
        ) {
            this.charFilters = charFilters;
            this.tokenizerFactory = tokenizerFactory;
            this.tokenFilters = tokenFilters;
        }

        @Override
        protected Reader initReader(String fieldName, Reader reader) {
            Reader current = reader;
            for (Function<Reader, Reader> filter : charFilters) {
                current = filter.apply(current);
            }
            return current;
        }

        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = tokenizerFactory.apply(fieldName);
            TokenStream stream = tokenizer;
            for (Function<TokenStream, TokenStream> filter : tokenFilters) {
                stream = filter.apply(stream);
            }
            return new TokenStreamComponents(tokenizer, stream);
        }
    }
}
