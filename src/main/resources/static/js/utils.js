// Helper to format datetime to readable time
function formatTime(start, end) {
    const startTime = new Date(start).toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit" });
    const endTime = new Date(end).toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit" });
    return `${startTime} - ${endTime}`;
}

// Helper to format date
function formatDate(dateTimeStr) {
    const date = new Date(dateTimeStr);
    return date.toLocaleDateString("en-US", {
        weekday: "short",   // "Thu"
        month: "short",     // "Apr"
        day: "numeric",     // "30"
        year: "numeric"     // "2026"
    }); // → "Thu, Apr 30, 2026"
}
