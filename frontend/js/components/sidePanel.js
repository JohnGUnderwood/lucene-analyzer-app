/**
 * Side panel component
 * Handles info panel open/close functionality
 */

/**
 * Open the side panel
 */
export function openSidePanel() {
    const panel = document.getElementById('sidePanel');
    if (panel) {
        panel.classList.add('open');
    }
}

/**
 * Close the side panel
 */
export function closeSidePanel() {
    const panel = document.getElementById('sidePanel');
    if (panel) {
        panel.classList.remove('open');
    }
}

// Make functions globally available for inline onclick handlers
window.openSidePanel = openSidePanel;
window.closeSidePanel = closeSidePanel;
