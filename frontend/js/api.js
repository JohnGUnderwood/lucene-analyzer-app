/**
 * API communication module
 * Handles all HTTP requests to the backend
 */

const API_BASE_URL = 'http://localhost:8181/api';

/**
 * Fetch all available analyzers
 * @returns {Promise<Array>} List of analyzer details
 */
export async function fetchAnalyzers() {
    try {
        const response = await fetch(`${API_BASE_URL}/analyzers`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching analyzers:', error);
        throw error;
    }
}

/**
 * Analyze text with specified analyzers
 * @param {Object} request - The analysis request object
 * @returns {Promise<Object>} Analysis response with tokens
 */
export async function analyzeText(request) {
    try {
        const response = await fetch(`${API_BASE_URL}/analyze`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request),
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Error analyzing text:', error);
        throw error;
    }
}
