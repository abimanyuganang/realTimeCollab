# Collaborative Editor

## Overview
The Collaborative Editor is a real-time collaboration tool that allows multiple users to edit documents simultaneously, similar to Google Docs. This application utilizes WebSocket technology for real-time communication and provides a user-friendly interface built with HTML, CSS, and JavaScript.

## Features
- Real-time document editing
- User session management
- Document state management
- WebSocket connection for live updates

## Project Structure
```
collaborative-editor
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── editor
│   │   │           ├── WebSocketServer.java
│   │   │           ├── DocumentManager.java
│   │   │           ├── UserSession.java
│   │   │           └── model
│   │   │               └── Document.java
│   │   ├── resources
│   │   │   └── static
│   │   │       ├── css
│   │   │       │   └── styles.css
│   │   │       ├── js
│   │   │       │   └── editor.js
│   │   │       └── index.html
│   │   └── webapp
│   │       └── WEB-INF
│   │           └── web.xml
├── pom.xml
└── README.md
```

## Getting Started
1. Clone the repository.
2. Navigate to the project directory.
3. Build the project using Maven: `mvn clean install`.
4. Run the application on your local server.

## Technologies Used
- Java
- Spring Framework
- WebSocket
- HTML/CSS/JavaScript
- Maven

## License
This project is licensed under the MIT License.