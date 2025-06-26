const socket = new WebSocket('ws://localhost:8080/collab');

socket.onopen = function(event) {
    console.log('WebSocket connection established');
};

socket.onmessage = function(event) {
    const message = JSON.parse(event.data);
    updateDocumentContent(message);
};

socket.onclose = function(event) {
    console.log('WebSocket connection closed');
};

function updateDocumentContent(message) {
    const documentArea = document.getElementById('editor');
    documentArea.value = message.content;
}

function sendMessage(content) {
    const message = {
        content: content,
        timestamp: new Date().toISOString()
    };
    socket.send(JSON.stringify(message));
}

document.getElementById('editor').addEventListener('input', function() {
    sendMessage(this.value);
});

