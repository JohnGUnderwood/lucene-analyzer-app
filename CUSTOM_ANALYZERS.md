# Custom Analyzer Feature

## Overview

The Lucene Analyzer App now supports custom analyzer definitions! You can create complex analysis pipelines by combining character filters, tokenizers, and token filters using JSON configuration.

## Quick Start

1. In the web interface, select the **"Custom Analyzer (JSON)"** radio button for either index or query analyzer
2. Enter your JSON analyzer definition in the textarea
3. Click **"Load Example"** to see a sample configuration
4. Click **"Analyze"** to test your custom analyzer

## JSON Structure

A custom analyzer definition consists of three main parts:

```json
{
  "name": "myCustomAnalyzer",
  "charFilters": [ /* optional */ ],
  "tokenizer": { /* required */ },
  "tokenFilters": [ /* optional */ ]
}
```

### Character Filters (Optional)

Applied before tokenization to transform the input text.

**Example:**
```json
"charFilters": [
  {
    "type": "htmlStrip",
    "ignoredTags": ["b", "i"]
  },
  {
    "type": "mapping",
    "mappings": {
      "&": "and",
      "@": "at"
    }
  }
]
```

**Supported Types:**
- `htmlStrip` - Removes HTML tags
- `icuNormalize` - Unicode normalization
- `mapping` - Character/string mappings
- `persian` - Persian character normalization

### Tokenizer (Required)

Breaks text into tokens. Every analyzer must have exactly one tokenizer.

**Example:**
```json
"tokenizer": {
  "type": "standard",
  "maxTokenLength": 255
}
```

**Supported Types:**
- `standard` - Standard Unicode text segmentation
- `whitespace` - Split on whitespace
- `keyword` - Entire input as one token
- `edgeGram` - Edge n-grams
- `nGram` - N-grams
- `regexCaptureGroup` - Regex with capture groups
- `regexSplit` - Split on regex matches
- `uaxUrlEmail` - Preserves URLs and emails

### Token Filters (Optional)

Transform tokens after tokenization. Applied in order.

**Example:**
```json
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
```

**Supported Types (25 total):**
- `lowercase` - Convert to lowercase
- `asciiFolding` - Remove accents
- `stopword` - Remove stopwords
- `length` - Filter by length
- `porterStemming` - Porter stemmer
- `kStemming` - K-stemmer
- `snowballStemming` - Multi-language stemmer
- `englishPossessive` - Remove 's
- `spanishPluralStemming` - Spanish plurals
- `stempel` - Polish stemmer
- `edgeGram` - Edge n-grams
- `nGram` - N-grams
- `shingle` - Multi-word tokens
- `wordDelimiterGraph` - Split camelCase, etc.
- `regex` - Pattern replacement
- `trim` - Remove whitespace
- `reverse` - Reverse strings
- `removeDuplicates` - Remove consecutive duplicates
- `icuFolding` - ICU Unicode folding
- `icuNormalizer` - ICU normalization
- `daitchMokotoffSoundex` - Phonetic encoding
- `flattenGraph` - Flatten token graphs
- `keywordRepeat` - Duplicate tokens

## Complete Examples

### 1. Basic English Analyzer
```json
{
  "name": "simpleEnglish",
  "tokenizer": {
    "type": "standard"
  },
  "tokenFilters": [
    {
      "type": "lowercase"
    },
    {
      "type": "stopword",
      "tokens": ["the", "a", "an", "and", "or", "but"],
      "ignoreCase": true
    },
    {
      "type": "porterStemming"
    }
  ]
}
```

### 2. Autocomplete Analyzer
```json
{
  "name": "autocomplete",
  "tokenizer": {
    "type": "edgeGram",
    "minGram": 2,
    "maxGram": 10
  },
  "tokenFilters": [
    {
      "type": "lowercase"
    }
  ]
}
```

### 3. Code Identifier Analyzer
```json
{
  "name": "codeAnalyzer",
  "tokenizer": {
    "type": "whitespace"
  },
  "tokenFilters": [
    {
      "type": "wordDelimiterGraph",
      "generateWordParts": true,
      "splitOnCaseChange": true,
      "preserveOriginal": false
    },
    {
      "type": "lowercase"
    },
    {
      "type": "flattenGraph"
    }
  ]
}
```

### 4. HTML Content Analyzer
```json
{
  "name": "htmlAnalyzer",
  "charFilters": [
    {
      "type": "htmlStrip"
    }
  ],
  "tokenizer": {
    "type": "standard"
  },
  "tokenFilters": [
    {
      "type": "lowercase"
    },
    {
      "type": "stopword",
      "tokens": ["the", "a", "an"],
      "ignoreCase": true
    }
  ]
}
```

### 5. Phonetic Search
```json
{
  "name": "phonetic",
  "tokenizer": {
    "type": "standard"
  },
  "tokenFilters": [
    {
      "type": "lowercase"
    },
    {
      "type": "daitchMokotoffSoundex",
      "originalTokens": "include"
    }
  ]
}
```

### 6. Multi-language with ICU
```json
{
  "name": "unicode",
  "charFilters": [
    {
      "type": "icuNormalize"
    }
  ],
  "tokenizer": {
    "type": "standard"
  },
  "tokenFilters": [
    {
      "type": "icuFolding"
    },
    {
      "type": "lowercase"
    }
  ]
}
```

## Common Patterns

### Autocomplete (Index)
Use edge grams with character limits:
```json
{
  "tokenizer": {"type": "edgeGram", "minGram": 2, "maxGram": 15},
  "tokenFilters": [{"type": "lowercase"}]
}
```

### Autocomplete (Query)
Standard tokenizer with lowercase only:
```json
{
  "tokenizer": {"type": "standard"},
  "tokenFilters": [{"type": "lowercase"}]
}
```

### Case-Insensitive Search
```json
{
  "tokenizer": {"type": "standard"},
  "tokenFilters": [{"type": "lowercase"}]
}
```

### Remove Short Tokens
```json
{
  "tokenFilters": [
    {"type": "lowercase"},
    {"type": "length", "min": 3, "max": 50}
  ]
}
```

### Full-Text Search
```json
{
  "tokenizer": {"type": "standard"},
  "tokenFilters": [
    {"type": "lowercase"},
    {"type": "stopword", "tokens": ["the", "a", "an"], "ignoreCase": true},
    {"type": "porterStemming"}
  ]
}
```

## Tips

1. **Order Matters**: Token filters are applied in the order specified
2. **Start Simple**: Begin with just lowercase, add complexity as needed
3. **Test Thoroughly**: Use the app to test with real data
4. **Stemming vs Folding**: Stemming reduces words to roots, folding handles accents/case
5. **Performance**: More filters = slower analysis
6. **Graph Filters**: Use `flattenGraph` after filters that create token graphs (like `wordDelimiterGraph`)

## Error Handling

If you see an error:
- Check JSON syntax (use a JSON validator)
- Verify all required properties are present
- Check property names match exactly (case-sensitive)
- Ensure tokenizer is present
- Validate regex patterns in regex-based components

## API Integration

The custom analyzer definitions are sent to the backend via:
```javascript
{
  "customIndexAnalyzer": { /* your definition */ },
  "customQueryAnalyzer": { /* your definition */ }
}
```

## Component Reference

See `custom-analyzer-examples.json` for:
- Complete list of all 37 supported components
- Detailed property descriptions
- Additional examples

## MongoDB Atlas Search Compatibility

These custom analyzers follow the same structure as MongoDB Atlas Search custom analyzers, making it easy to:
1. Design your analyzer in this tool
2. Test it with sample data
3. Copy the JSON directly to your Atlas Search index definition

For more information on Atlas Search analyzers, see:
https://www.mongodb.com/docs/atlas/atlas-search/analyzers/custom/
