package com.mongodb.lucene.service;

import com.mongodb.lucene.model.AnalyzerDetail;
import com.mongodb.lucene.model.AutocompleteConfig;
import com.mongodb.lucene.model.TokenInfo;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.bg.BulgarianAnalyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.ca.CatalanAnalyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.ckb.SoraniAnalyzer;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.da.DanishAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.eu.BasqueAnalyzer;
import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.analysis.fi.FinnishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.ga.IrishAnalyzer;
import org.apache.lucene.analysis.gl.GalicianAnalyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.analysis.hu.HungarianAnalyzer;
import org.apache.lucene.analysis.hy.ArmenianAnalyzer;
import org.apache.lucene.analysis.id.IndonesianAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.lv.LatvianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.TruncateTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.no.NorwegianAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.sv.SwedishAnalyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tr.TurkishAnalyzer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Service
public class AnalyzerService {

    private static final String CATEGORY_BASE = "base";
    private static final String CATEGORY_LANGUAGE = "language";

    /**
     * Get all available analyzers
     */
    public List<AnalyzerDetail> getAvailableAnalyzers() {
        List<AnalyzerDetail> analyzers = new ArrayList<>();

        // Base analyzers
        analyzers.add(new AnalyzerDetail("lucene.standard", CATEGORY_BASE));
        analyzers.add(new AnalyzerDetail("lucene.whitespace", CATEGORY_BASE));
        analyzers.add(new AnalyzerDetail("lucene.simple", CATEGORY_BASE));
        analyzers.add(new AnalyzerDetail("lucene.keyword", CATEGORY_BASE));

        // Language-specific analyzers
        analyzers.add(new AnalyzerDetail("lucene.arabic", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.armenian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.basque", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.bengali", CATEGORY_LANGUAGE, true, "(not yet supported)"));
        analyzers.add(new AnalyzerDetail("lucene.brazilian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.bulgarian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.catalan", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.cjk", CATEGORY_LANGUAGE, false, "(Chinese, Japanese, Korean)"));
        analyzers.add(new AnalyzerDetail("lucene.czech", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.danish", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.dutch", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.english", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.finnish", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.french", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.galician", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.german", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.greek", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.hindi", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.hungarian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.indonesian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.irish", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.italian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.latvian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.norwegian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.persian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.portuguese", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.romanian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.russian", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.sorani", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.spanish", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.swedish", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.thai", CATEGORY_LANGUAGE));
        analyzers.add(new AnalyzerDetail("lucene.turkish", CATEGORY_LANGUAGE));

        return analyzers;
    }

    /**
     * Get analyzer instance by name
     */
    public Analyzer getAnalyzer(String analyzerName) {
        switch (analyzerName.toLowerCase()) {
            // Base analyzers - use empty stopword set for standard to match Atlas Search behavior
            case "lucene.standard":
                return new StandardAnalyzer(CharArraySet.EMPTY_SET);
            case "lucene.whitespace":
                return new WhitespaceAnalyzer();
            case "lucene.simple":
                return new SimpleAnalyzer();
            case "lucene.keyword":
                return new KeywordAnalyzer();

            // Language-specific analyzers
            case "lucene.arabic":
                return new ArabicAnalyzer();
            case "lucene.armenian":
                return new ArmenianAnalyzer();
            case "lucene.basque":
                return new BasqueAnalyzer();
            case "lucene.brazilian":
                return new BrazilianAnalyzer();
            case "lucene.bulgarian":
                return new BulgarianAnalyzer();
            case "lucene.catalan":
                return new CatalanAnalyzer();
            case "lucene.cjk":
                return new CJKAnalyzer();
            case "lucene.czech":
                return new CzechAnalyzer();
            case "lucene.danish":
                return new DanishAnalyzer();
            case "lucene.dutch":
                return new DutchAnalyzer();
            case "lucene.english":
                return new EnglishAnalyzer();
            case "lucene.finnish":
                return new FinnishAnalyzer();
            case "lucene.french":
                return new FrenchAnalyzer();
            case "lucene.galician":
                return new GalicianAnalyzer();
            case "lucene.german":
                return new GermanAnalyzer();
            case "lucene.greek":
                return new GreekAnalyzer();
            case "lucene.hindi":
                return new HindiAnalyzer();
            case "lucene.hungarian":
                return new HungarianAnalyzer();
            case "lucene.indonesian":
                return new IndonesianAnalyzer();
            case "lucene.irish":
                return new IrishAnalyzer();
            case "lucene.italian":
                return new ItalianAnalyzer();
            case "lucene.latvian":
                return new LatvianAnalyzer();
            case "lucene.norwegian":
                return new NorwegianAnalyzer();
            case "lucene.persian":
                return new PersianAnalyzer();
            case "lucene.portuguese":
                return new PortugueseAnalyzer();
            case "lucene.romanian":
                return new RomanianAnalyzer();
            case "lucene.russian":
                return new RussianAnalyzer();
            case "lucene.sorani":
                return new SoraniAnalyzer();
            case "lucene.spanish":
                return new SpanishAnalyzer();
            case "lucene.swedish":
                return new SwedishAnalyzer();
            case "lucene.thai":
                return new ThaiAnalyzer();
            case "lucene.turkish":
                return new TurkishAnalyzer();

            default:
                throw new IllegalArgumentException("Unknown analyzer: " + analyzerName);
        }
    }

    /**
     * Analyze text and return tokens
     */
    public Set<TokenInfo> analyzeText(Analyzer analyzer, String text, boolean isQuery, 
                                      boolean useAutocomplete, AutocompleteConfig config) throws IOException {
        Set<String> tokenSet = new LinkedHashSet<>();
        TokenStream stream = analyzer.tokenStream(null, new StringReader(text));

        try {
            stream.reset();

            if (useAutocomplete) {
                if (isQuery) {
                    // For queries, just tokenize and truncate to max grams
                    CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
                    while (stream.incrementToken()) {
                        String tokenString = termAtt.toString();
                        // Truncate to max grams
                        TokenStream tokenStream = new KeywordTokenizer();
                        ((KeywordTokenizer) tokenStream).setReader(new StringReader(tokenString));
                        truncateAndAddTokens(tokenStream, tokenSet, config.getMaxGrams());
                        // Also add original token
                        tokenSet.add(tokenString);
                    }
                } else {
                    // For index: Apply shingle filter then n-grams
                    ShingleFilter shingleFilter = new ShingleFilter(stream, 2, 3);
                    shingleFilter.setOutputUnigramsIfNoShingles(true);
                    CharTermAttribute termAtt = shingleFilter.addAttribute(CharTermAttribute.class);

                    while (shingleFilter.incrementToken()) {
                        String tokenString = termAtt.toString();

                        // Preserve original token
                        TokenStream originalStream = new KeywordTokenizer();
                        ((KeywordTokenizer) originalStream).setReader(new StringReader(tokenString));
                        truncateAndAddTokens(originalStream, tokenSet, config.getMaxGrams());

                        // Apply n-gram tokenization
                        TokenStream nGramStream = getNGramTokenStream(tokenString, config);
                        truncateAndAddTokens(nGramStream, tokenSet, config.getMaxGrams());
                    }
                }
            } else {
                // Simple tokenization without autocomplete
                CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
                while (stream.incrementToken()) {
                    tokenSet.add(termAtt.toString());
                }
            }

            stream.end();
        } finally {
            stream.close();
        }

        // Convert to TokenInfo objects
        Set<TokenInfo> tokens = new LinkedHashSet<>();
        for (String token : tokenSet) {
            tokens.add(new TokenInfo(token));
        }
        return tokens;
    }

    /**
     * Create n-gram token stream based on configuration
     */
    private TokenStream getNGramTokenStream(String text, AutocompleteConfig config) throws IOException {
        KeywordTokenizer tokenizer = new KeywordTokenizer();
        tokenizer.setReader(new StringReader(text));

        if ("edgeGram".equals(config.getAutocompleteType())) {
            return new EdgeNGramTokenFilter(tokenizer, config.getMinGrams(), config.getMaxGrams(), false);
        } else if ("nGram".equals(config.getAutocompleteType())) {
            return new NGramTokenFilter(tokenizer, config.getMinGrams(), config.getMaxGrams(), false);
        } else {
            throw new IllegalArgumentException("Invalid autocomplete type: " + config.getAutocompleteType());
        }
    }

    /**
     * Truncate token stream and add tokens to set
     */
    private void truncateAndAddTokens(TokenStream stream, Set<String> tokenSet, int maxLength) throws IOException {
        TruncateTokenFilter truncated = new TruncateTokenFilter(stream, maxLength);
        try {
            truncated.reset();
            CharTermAttribute termAtt = truncated.addAttribute(CharTermAttribute.class);
            while (truncated.incrementToken()) {
                tokenSet.add(termAtt.toString());
            }
            truncated.end();
        } finally {
            truncated.close();
        }
    }
}
