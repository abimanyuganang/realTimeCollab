class CollaborativeEditor {
    constructor() {
        this.editor = document.getElementById('editor');
        this.status = document.getElementById('connection-status');
        this.userCount = document.getElementById('user-count');
        this.ws = null;
        this.connectWebSocket();
        this.setupEventListeners();
    }

    connectWebSocket() {
        try {
            console.log('Attempting WebSocket connection...');
            this.ws = new WebSocket('ws://localhost:5500/collaborative-editor/ws');
            
            this.ws.onopen = () => {
                console.log('%cWebSocket Connected Successfully', 'color: green; font-weight: bold');
                this.updateStatus(true);
            };

            this.ws.onclose = (event) => {
                console.log('%cWebSocket Disconnected', 'color: red; font-weight: bold');
                console.log('Close code:', event.code);
                console.log('Close reason:', event.reason);
                this.updateStatus(false);
                setTimeout(() => this.connectWebSocket(), 3000);
            };

            this.ws.onerror = (error) => {
                console.error('WebSocket Error:', error);
                console.log('WebSocket ready state:', this.ws.readyState);
                this.updateStatus(false);
            };

            this.ws.onmessage = (event) => {
                console.log('%cReceived message:', 'color: blue', event.data);
                try {
                    if (event.data.startsWith('{')) {
                        const data = JSON.parse(event.data);
                        console.log('Parsed message:', data);
                        this.handleMessage(data);
                    } else {
                        console.log('System message:', event.data);
                        if (event.data === 'connected') {
                            this.updateStatus(true);
                        }
                    }
                } catch (e) {
                    console.error('Error parsing message:', e);
                }
            };

        } catch (error) {
            console.error('Failed to create WebSocket:', error);
        }
    }

    setupEventListeners() {
        let timeout = null;
        this.editor.addEventListener('input', () => {
            clearTimeout(timeout);
            timeout = setTimeout(() => {
                this.sendChange();
            }, 100);
        });
    }

    sendChange() {
        if (this.ws.readyState === WebSocket.OPEN) {
            const message = {
                type: 'change',
                content: this.editor.value,
                timestamp: Date.now()
            };
            try{
                const messageString = JSON.stringify(message);
                console.log('Sending:', message);
                this.ws.send(messageString);
            } catch (error) {
                console.error('Error sending message:', error);
            }
        } else {
            console.warn('WebSocket is not open. Current state:', this.ws.readyState);
        }
    }

    handleMessage(data) {
        console.log('Handling message:', data);
        switch (data.type) {
            case 'content':
            case 'change':
                if (this.editor.value !== data.content) {
                    const cursorPosition = this.editor.selectionStart;
                    this.editor.value = data.content;
                    this.editor.setSelectionRange(cursorPosition, cursorPosition);
                    console.log('Editor content updated:', data.content);
                }
                break;
            case 'users':
                this.userCount.textContent = `Connected Users: ${data.count}`;
                break;
        }
    }

    updateStatus(connected) {
        this.status.textContent = connected ? 'Connected' : 'Disconnected';
        this.status.className = connected ? 'connected' : 'disconnected';
    }

}

// Initialize when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new CollaborativeEditor();
});