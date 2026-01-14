package com.mongodb.lucene.controller;

import com.mongodb.lucene.model.*;
import com.mongodb.lucene.service.AnalyzerService;
import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AnalyzerController {

    @Autowired
    private AnalyzerService analyzerService;

    /**
     * Get all available analyzers
     */
    @GetMapping("/analyzers")
    public ResponseEntity<List<AnalyzerDetail>> getAnalyzers() {
        return ResponseEntity.ok(analyzerService.getAvailableAnalyzers());
    }

    /**
     * Analyze text with specified analyzers
     */
    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyzeText(@RequestBody AnalyzeRequest request) {
        try {
            // Get analyzers
            Analyzer indexAnalyzer = analyzerService.getAnalyzer(request.getIndexAnalyzer());
            Analyzer queryAnalyzer = analyzerService.getAnalyzer(request.getQueryAnalyzer());

            // Analyze index text
            Set<TokenInfo> indexTokens = analyzerService.analyzeText(
                indexAnalyzer,
                request.getIndexText(),
                false,
                request.isUseAutocomplete(),
                request.getAutocompleteConfig()
            );

            // Analyze query text
            Set<TokenInfo> queryTokens = analyzerService.analyzeText(
                queryAnalyzer,
                request.getQueryText(),
                true,
                request.isUseAutocomplete(),
                request.getAutocompleteConfig()
            );

            // Find matching tokens
            Set<String> indexTokenStrings = indexTokens.stream()
                .map(TokenInfo::getText)
                .collect(Collectors.toSet());
            Set<String> queryTokenStrings = queryTokens.stream()
                .map(TokenInfo::getText)
                .collect(Collectors.toSet());
            
            Set<String> matchingTokens = new HashSet<>(indexTokenStrings);
            matchingTokens.retainAll(queryTokenStrings);

            // Mark matched tokens
            indexTokens.forEach(token -> token.setMatched(matchingTokens.contains(token.getText())));
            queryTokens.forEach(token -> token.setMatched(matchingTokens.contains(token.getText())));

            // Create response
            AnalyzeResponse response = new AnalyzeResponse(
                new ArrayList<>(indexTokens),
                new ArrayList<>(queryTokens),
                matchingTokens,
                request.getIndexAnalyzer()
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
