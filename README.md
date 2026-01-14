# Lucene Analyzer Application
[The code in this repository was created by Anthropic Claude Sonnet 4.5 at the behest, and under the instruction, of John Underwood]

A web application for visualizing how Lucene analyzers tokenize text for search indexing and querying. This tool helps users understand how different analyzers break down text and which tokens match between indexed content and search queries.

## Features

- **40+ Lucene Analyzers**: Test standard, whitespace, simple, keyword, and language-specific analyzers
- **Visual Token Display**: See exactly how text is tokenized with color-coded matching
- **Autocomplete Support**: Test edgeGram and nGram tokenization for autocomplete fields
- **Side-by-side Comparison**: Compare index analyzer vs query analyzer behavior
- **Interactive UI**: Clean, responsive interface built with vanilla JavaScript

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.1**
- **Apache Lucene 9.9.1**
- **Maven**

### Frontend
- **HTML5**
- **CSS3**
- **Vanilla JavaScript (ES6+)**
- **Fetch API**

## Project Structure

```
lucene-analyzer-app/
├── backend/                          # Spring Boot REST API
│   ├── src/main/java/com/mongodb/lucene/
│   │   ├── LuceneAnalyzerApplication.java
│   │   ├── controller/
│   │   │   └── AnalyzerController.java
│   │   ├── service/
│   │   │   └── AnalyzerService.java
│   │   ├── model/
│   │   │   ├── AnalyzeRequest.java
│   │   │   ├── AnalyzeResponse.java
│   │   │   ├── AnalyzerDetail.java
│   │   │   ├── AutocompleteConfig.java
│   │   │   └── TokenInfo.java
│   │   └── config/
│   │       └── CorsConfig.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── frontend/                         # Vanilla JS frontend
    ├── index.html
    ├── css/
    │   ├── main.css
    │   └── components.css
    └── js/
        ├── app.js
        ├── api.js
        └── components/
            ├── tokenCard.js
            └── sidePanel.js
```

## Prerequisites

### Backend
- **Java 17 or higher**
- **Maven 3.6+**

### Frontend
- Any modern web browser (Chrome, Firefox, Safari, Edge)
- A simple HTTP server (see options below)

## Installation & Setup

### 1. Clone or Navigate to Project

```bash
cd lucene-analyzer-app
```

### 2. Backend Setup

#### Build and Run with Maven

```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend API will start on **http://localhost:8080**

#### Verify Backend is Running

Open your browser or use curl:
```bash
curl http://localhost:8080/api/analyzers
```

You should see a JSON response with available analyzers.

### 3. Frontend Setup

The frontend is pure HTML/CSS/JS and needs to be served via HTTP (not file://).

#### Option 1: Python HTTP Server (Easiest)

```bash
# Navigate to frontend directory
cd frontend

# Python 3
python3 -m http.server 3000

# Or Python 2
python -m SimpleHTTPServer 3000
```

#### Option 2: Node.js HTTP Server

```bash
# Install http-server globally (one time)
npm install -g http-server

# Navigate to frontend directory
cd frontend

# Start server
http-server -p 3000
```

#### Option 3: VS Code Live Server

1. Install "Live Server" extension in VS Code
2. Right-click on `index.html`
3. Select "Open with Live Server"

#### Option 4: PHP Server

```bash
cd frontend
php -S localhost:3000
```

The frontend will be available at **http://localhost:3000**

## Usage

1. **Open the application** in your browser at http://localhost:3000

2. **Enter text to analyze** in the "Text to Analyze" field (default: "The quick brown fox jumps over the lazy dog")

3. **Enter a search query** in the "Query to Analyze" field (default: "jumping fox")

4. **Select analyzers** from the dropdowns:
   - **Index Analyzer**: How the text is tokenized when indexed
   - **Query Analyzer**: How the search query is tokenized

5. **(Optional) Enable autocomplete**: Check the box to test edgeGram or nGram tokenization

6. **Click "Analyze"** to see the results

7. **View tokens**: 
   - **Green tokens** = Matches between index and query (documents would be found!)
   - **White tokens** = No match

8. **Try different analyzers** to see how they affect tokenization

## API Documentation

### GET /api/analyzers

Returns list of all available analyzers.

**Response:**
```json
[
  {
    "name": "lucene.standard",
    "category": "base",
    "disabled": false,
    "additionalLabel": ""
  },
  ...
]
```

### POST /api/analyze

Analyzes text with specified analyzers.

**Request Body:**
```json
{
  "indexText": "The quick brown fox",
  "queryText": "quick fox",
  "indexAnalyzer": "lucene.standard",
  "queryAnalyzer": "lucene.standard",
  "useAutocomplete": false,
  "autocompleteConfig": {
    "autocompleteType": "edgeGram",
    "minGrams": 3,
    "maxGrams": 15
  }
}
```

**Response:**
```json
{
  "indexTokens": [
    {"text": "quick", "length": 5, "matched": true},
    {"text": "brown", "length": 5, "matched": false},
    {"text": "fox", "length": 3, "matched": true}
  ],
  "queryTokens": [
    {"text": "quick", "length": 5, "matched": true},
    {"text": "fox", "length": 3, "matched": true}
  ],
  "matchingTokens": ["quick", "fox"],
  "analyzerUsed": "lucene.standard"
}
```

## Available Analyzers

### Base Analyzers
- **lucene.standard** - Lowercase, removes whitespace (no stopwords in this implementation)
- **lucene.whitespace** - Only removes whitespace, case-sensitive
- **lucene.simple** - Lowercase, removes whitespace
- **lucene.keyword** - Treats entire string as single token

### Language-Specific Analyzers (40+)
Arabic, Armenian, Basque, Brazilian, Bulgarian, Catalan, CJK, Czech, Danish, Dutch, English, Finnish, French, Galician, German, Greek, Hindi, Hungarian, Indonesian, Irish, Italian, Latvian, Norwegian, Persian, Portuguese, Romanian, Russian, Sorani, Spanish, Swedish, Thai, Turkish, and more.

## Configuration

### Backend Port

Edit `backend/src/main/resources/application.properties`:
```properties
server.port=8080
```

### Frontend API URL

Edit `frontend/js/api.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

## Troubleshooting

### Backend Issues

**Error: Port 8080 already in use**
- Change port in `application.properties`
- Or kill the process using port 8080

**Error: Java version mismatch**
- Ensure Java 17+ is installed: `java -version`
- Set JAVA_HOME if needed

### Frontend Issues

**Error: CORS policy blocking requests**
- Make sure backend is running on http://localhost:8080
- Check browser console for specific errors
- Verify CorsConfig.java is properly configured

**Error: Module not found**
- Ensure frontend is served via HTTP (not file://)
- Check that all JS files exist in correct paths

**No analyzers showing in dropdowns**
- Check browser console for errors
- Verify backend is running and accessible
- Test API endpoint directly: http://localhost:8080/api/analyzers

## Development

### Backend Development

```bash
cd backend

# Run with auto-reload (Spring Boot DevTools)
mvn spring-boot:run

# Run tests
mvn test

# Package as JAR
mvn package
```

### Frontend Development

Since it's vanilla JS, just edit files and refresh the browser. No build step required!

## Deployment

### Backend

```bash
# Build JAR
cd backend
mvn clean package

# Run JAR
java -jar target/lucene-analyzer-1.0.0.jar
```

### Frontend

Simply copy the `frontend/` directory to any static file hosting:
- AWS S3
- Netlify
- Vercel
- GitHub Pages
- Or serve from Spring Boot (add to src/main/resources/static)

## License

This project is created for MongoDB specialists and educational purposes.

## Support

For issues or questions, please contact the development team or file an issue in the repository.

---

## Quick Start Commands

```bash
# Terminal 1 - Start Backend
cd backend
mvn spring-boot:run

# Terminal 2 - Start Frontend
cd frontend
python3 -m http.server 3000

# Open browser to http://localhost:3000
```

Enjoy analyzing with Lucene! 🚀
