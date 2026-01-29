/**
 * Main application logic
 * Handles initialization, user interactions, and orchestrates the analyzer functionality
 */

import { fetchAnalyzers, analyzeText } from './api.js';
import { renderTokens } from './components/tokenCard.js';
import './components/sidePanel.js';

// State
let analyzers = [];

/**
 * Initialize the application
 */
async function init() {
    try {
        // Fetch available analyzers
        analyzers = await fetchAnalyzers();
        
        // Populate analyzer dropdowns
        populateAnalyzerSelects();
        
        // Set up event listeners
        setupEventListeners();
        
        console.log('Application initialized successfully');
    } catch (error) {
        console.error('Failed to initialize application:', error);
        alert('Failed to load analyzers. Please ensure the backend server is running on http://localhost:8080');
    }
}

/**
 * Populate analyzer select dropdowns
 */
function populateAnalyzerSelects() {
    const indexSelect = document.getElementById('indexAnalyzer');
    const querySelect = document.getElementById('queryAnalyzer');
    
    // Clear existing options
    indexSelect.innerHTML = '';
    querySelect.innerHTML = '';
    
    // Group analyzers by category
    const baseAnalyzers = analyzers.filter(a => a.category === 'base');
    const languageAnalyzers = analyzers.filter(a => a.category === 'language');
    
    // Add base analyzers
    baseAnalyzers.forEach(analyzer => {
        addOptionToSelect(indexSelect, analyzer);
        addOptionToSelect(querySelect, analyzer);
    });
    
    // Add language analyzers group
    if (languageAnalyzers.length > 0) {
        const indexGroup = document.createElement('optgroup');
        indexGroup.label = 'Language Analyzers';
        const queryGroup = document.createElement('optgroup');
        queryGroup.label = 'Language Analyzers';
        
        languageAnalyzers.forEach(analyzer => {
            addOptionToSelect(indexGroup, analyzer);
            addOptionToSelect(queryGroup, analyzer);
        });
        
        indexSelect.appendChild(indexGroup);
        querySelect.appendChild(queryGroup);
    }
    
    // Set default selections
    indexSelect.value = 'lucene.standard';
    querySelect.value = 'lucene.standard';
}

/**
 * Add an option to a select element
 */
function addOptionToSelect(selectOrGroup, analyzer) {
    const option = document.createElement('option');
    option.value = analyzer.name;
    option.textContent = analyzer.name + (analyzer.additionalLabel ? ' ' + analyzer.additionalLabel : '');
    option.disabled = analyzer.disabled;
    selectOrGroup.appendChild(option);
}

/**
 * Set up event listeners
 */
function setupEventListeners() {
    // Analyze button
    document.getElementById('analyzeBtn').addEventListener('click', handleAnalyze);
    
    // Reset button
    document.getElementById('resetBtn').addEventListener('click', handleReset);
    
    // Autocomplete checkbox
    document.getElementById('useAutocomplete').addEventListener('change', handleAutocompleteToggle);
    
    // Radio buttons for analyzer type
    document.querySelectorAll('input[name="indexAnalyzerType"]').forEach(radio => {
        radio.addEventListener('change', () => toggleAnalyzerInput('index'));
    });
    
    document.querySelectorAll('input[name="queryAnalyzerType"]').forEach(radio => {
        radio.addEventListener('change', () => toggleAnalyzerInput('query'));
    });
    
    // Load example buttons
    document.getElementById('loadExampleIndex').addEventListener('click', () => loadExampleDefinition('index'));
    document.getElementById('loadExampleQuery').addEventListener('click', () => loadExampleDefinition('query'));
}

/**
 * Toggle between predefined and custom analyzer inputs
 */
function toggleAnalyzerInput(type) {
    const isPredefined = document.querySelector(`input[name="${type}AnalyzerType"]:checked`).value === 'predefined';
    const selectId = type === 'index' ? 'indexAnalyzer' : 'queryAnalyzer';
    const textareaId = type === 'index' ? 'customIndexAnalyzer' : 'customQueryAnalyzer';
    const btnId = type === 'index' ? 'loadExampleIndex' : 'loadExampleQuery';
    
    document.getElementById(selectId).style.display = isPredefined ? 'block' : 'none';
    document.getElementById(textareaId).style.display = isPredefined ? 'none' : 'block';
    document.getElementById(btnId).style.display = isPredefined ? 'none' : 'inline-block';
}

/**
 * Load example custom analyzer definition
 */
function loadExampleDefinition(type) {
    const example = {
        "name": "myCustomAnalyzer",
        "charFilters": [
            {
                "type": "mapping",
                "mappings": {
                    "&": "and"
                }
            }
        ],
        "tokenizer": {
            "type": "standard",
            "maxTokenLength": 255
        },
        "tokenFilters": [
            {
                "type": "lowercase"
            },
            {
                "type": "stopword",
                "tokens": ["the", "a", "an"],
                "ignoreCase": true
            },
            {
                "type": "length",
                "min": 2,
                "max": 100
            }
        ]
    };
    
    const textareaId = type === 'index' ? 'customIndexAnalyzer' : 'customQueryAnalyzer';
    document.getElementById(textareaId).value = JSON.stringify(example, null, 2);
}

/**
 * Handle autocomplete checkbox toggle
 */
function handleAutocompleteToggle(event) {
    const autocompleteOptions = document.getElementById('autocompleteOptions');
    autocompleteOptions.style.display = event.target.checked ? 'block' : 'none';
}

/**
 * Handle analyze button click
 */
async function handleAnalyze() {
    const indexText = document.getElementById('indexText').value.trim();
    const queryText = document.getElementById('queryText').value.trim();
    const useAutocomplete = document.getElementById('useAutocomplete').checked;
    
    // Get analyzer selection
    const indexType = document.querySelector('input[name="indexAnalyzerType"]:checked').value;
    const queryType = document.querySelector('input[name="queryAnalyzerType"]:checked').value;
    
    let indexAnalyzer = null;
    let queryAnalyzer = null;
    let customIndexAnalyzer = null;
    let customQueryAnalyzer = null;
    
    // Validation
    if (!indexText || !queryText) {
        alert('Please enter both index text and query text');
        return;
    }
    
    // Get index analyzer
    if (indexType === 'predefined') {
        indexAnalyzer = document.getElementById('indexAnalyzer').value;
        if (!indexAnalyzer) {
            alert('Please select an index analyzer');
            return;
        }
    } else {
        const customDef = document.getElementById('customIndexAnalyzer').value.trim();
        if (!customDef) {
            alert('Please enter a custom index analyzer definition');
            return;
        }
        try {
            customIndexAnalyzer = JSON.parse(customDef);
        } catch (e) {
            alert('Invalid JSON in custom index analyzer: ' + e.message);
            return;
        }
    }
    
    // Get query analyzer
    if (queryType === 'predefined') {
        queryAnalyzer = document.getElementById('queryAnalyzer').value;
        if (!queryAnalyzer) {
            alert('Please select a query analyzer');
            return;
        }
    } else {
        const customDef = document.getElementById('customQueryAnalyzer').value.trim();
        if (!customDef) {
            alert('Please enter a custom query analyzer definition');
            return;
        }
        try {
            customQueryAnalyzer = JSON.parse(customDef);
        } catch (e) {
            alert('Invalid JSON in custom query analyzer: ' + e.message);
            return;
        }
    }
    
    // Build request
    const request = {
        indexText,
        queryText,
        indexAnalyzer,
        queryAnalyzer,
        customIndexAnalyzer,
        customQueryAnalyzer,
        useAutocomplete,
        autocompleteConfig: {
            autocompleteType: document.getElementById('autocompleteType').value,
            minGrams: parseInt(document.getElementById('minGrams').value),
            maxGrams: parseInt(document.getElementById('maxGrams').value),
        }
    };
    
    try {
        // Show loading state
        const analyzeBtn = document.getElementById('analyzeBtn');
        const originalText = analyzeBtn.textContent;
        analyzeBtn.textContent = 'Analyzing...';
        analyzeBtn.disabled = true;
        
        // Call API
        const response = await analyzeText(request);
        
        // Display results
        displayResults(response);
        
        // Reset button state
        analyzeBtn.textContent = originalText;
        analyzeBtn.disabled = false;
    } catch (error) {
        alert('Error analyzing text. Please try again.');
        const analyzeBtn = document.getElementById('analyzeBtn');
        analyzeBtn.textContent = 'Analyze';
        analyzeBtn.disabled = false;
    }
}

/**
 * Display analysis results
 */
function displayResults(response) {
    const outputCard = document.getElementById('outputCard');
    const indexTokensContainer = document.getElementById('indexTokens');
    const queryTokensContainer = document.getElementById('queryTokens');
    const indexAlert = document.getElementById('indexAlert');
    const queryAlert = document.getElementById('queryAlert');
    
    // Show output card
    outputCard.style.display = 'block';
    
    // Display index tokens
    if (response.indexTokens && response.indexTokens.length > 0) {
        renderTokens(response.indexTokens, indexTokensContainer);
        indexAlert.style.display = 'block';
        indexAlert.className = 'alert alert-success';
        indexAlert.innerHTML = `
            <strong>Huzzah!</strong> ${response.indexTokens.length} unique tokens were created.
            <br>You used the <strong>${response.analyzerUsed}</strong> analyzer, try a different one to see changes.
        `;
    } else {
        indexTokensContainer.innerHTML = '';
        indexAlert.style.display = 'block';
        indexAlert.className = 'alert alert-secondary';
        indexAlert.innerHTML = `
            <strong>No non-stopword tokens were returned.</strong> Try again with more text?
        `;
    }
    
    // Display query tokens
    if (response.queryTokens && response.queryTokens.length > 0) {
        renderTokens(response.queryTokens, queryTokensContainer);
        queryAlert.style.display = 'block';
        queryAlert.className = 'alert alert-success';
        queryAlert.innerHTML = `
            <strong>Huzzah!</strong> ${response.queryTokens.length} unique tokens were created.
        `;
    } else {
        queryTokensContainer.innerHTML = '';
        queryAlert.style.display = 'block';
        queryAlert.className = 'alert alert-secondary';
        queryAlert.innerHTML = `
            <strong>No non-stopword tokens were returned.</strong> Try again with more text?
        `;
    }
    
    // Scroll to results
    outputCard.scrollIntoView({ behavior: 'smooth' });
}

/**
 * Handle reset button click
 */
function handleReset() {
    // Hide output card
    document.getElementById('outputCard').style.display = 'none';
    
    // Clear containers
    document.getElementById('indexTokens').innerHTML = '';
    document.getElementById('queryTokens').innerHTML = '';
    
    // Scroll to top
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// Initialize app when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    init();
}
