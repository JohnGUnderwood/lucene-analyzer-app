/**
 * Token card component
 * Creates visual representation of a token
 */

/**
 * Create a token card element
 * @param {Object} tokenInfo - Token information {text, length, matched}
 * @returns {HTMLElement} Token card element
 */
export function createTokenCard(tokenInfo) {
    const card = document.createElement('div');
    card.className = 'token-card';
    
    if (tokenInfo.matched) {
        card.classList.add('matched');
    }
    
    const heading = document.createElement('h6');
    heading.textContent = tokenInfo.text;
    
    const length = document.createElement('p');
    length.className = tokenInfo.matched ? '' : 'text-muted';
    length.textContent = `${tokenInfo.length} characters`;
    
    card.appendChild(heading);
    card.appendChild(length);
    
    return card;
}

/**
 * Render tokens to a container
 * @param {Array} tokens - Array of token info objects
 * @param {HTMLElement} container - Container element to render tokens into
 */
export function renderTokens(tokens, container) {
    // Clear existing content
    container.innerHTML = '';
    
    // Create and append token cards
    tokens.forEach(token => {
        const card = createTokenCard(token);
        container.appendChild(card);
    });
}
