# Realtime Collaborative Editor

## Overview
The Realtime Collaborative Editor is a web-based application that allows multiple users to edit documents simultaneously in real-time. Built using Java for the backend and HTML, CSS, and JavaScript for the frontend, this project leverages WebSocket technology to enable live collaboration.

## Features
- Real-time document editing
- Multiple user support
- Automatic synchronization of document changes
- User-friendly interface

## Architecture
The project is structured into two main components: the backend and the frontend.

### Backend
- **WebSocket Endpoint**: Manages real-time connections and message handling.
- **Document Service**: Contains business logic for document management.
- **Document Model**: Represents the structure of a document.
- **Utility Class**: Provides methods for JSON conversion using Gson.

### Frontend
- **HTML**: The main structure of the application.
- **CSS**: Styles for a responsive and aesthetic user interface.
- **JavaScript**: Handles user interactions and WebSocket connections.

## Technologies Used
- **Backend**: Java, WebSocket, Gson, Apache Tomcat
- **Frontend**: HTML, CSS, JavaScript

## Setup Instructions

### Backend
1. Navigate to the `backend` directory.
2. Ensure you have Maven installed.
3. Run `mvn clean install` to build the project.
4. Configure the `application.properties` file as needed.
5. Deploy the application on Apache Tomcat.

### Frontend
1. Navigate to the `frontend` directory.
2. Ensure you have Node.js and npm installed.
3. Run `npm install` to install dependencies.
4. Open `public/index.html` in a web browser to view the application.

## Testing WebSocket Connection
You can test the WebSocket connection using Postman:
1. Open Postman and create a new WebSocket request.
2. Enter the WebSocket URL (e.g., `ws://localhost:8080/your-websocket-endpoint`).
3. Connect and send messages to test the real-time functionality.

## Contribution
Contributions are welcome! Please fork the repository and submit a pull request for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.