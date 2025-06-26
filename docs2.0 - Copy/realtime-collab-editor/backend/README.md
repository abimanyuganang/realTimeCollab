# Backend Real-Time Collaborative Editor

This document provides an overview of the backend implementation for the Real-Time Collaborative Editor project. The backend is built using Java and utilizes WebSocket for real-time communication between clients.

## Project Structure

The backend project is organized as follows:

```
backend
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── websocket
│   │   │           │   └── CollaborationEndpoint.java
│   │   │           ├── service
│   │   │           │   └── DocumentService.java
│   │   │           ├── model
│   │   │           │   └── Document.java
│   │   │           └── util
│   │   │               └── GsonUtil.java
│   │   └── resources
│   │       └── application.properties
├── pom.xml
└── README.md
```

## Technologies Used

- **Java**: The primary programming language for the backend.
- **WebSocket**: For real-time communication between clients.
- **Gson**: For JSON serialization and deserialization.
- **Apache Tomcat**: As the web server to deploy the application.

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd realtime-collab-editor/backend
   ```

2. **Build the Project**:
   Ensure you have Maven installed. Run the following command to build the project:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   Deploy the application on Apache Tomcat. Ensure that the `application.properties` file is configured correctly for your environment.

4. **Testing WebSocket Connection**:
   You can test the WebSocket connection using Postman:
   - Open Postman and create a new WebSocket request.
   - Enter the WebSocket URL (e.g., `ws://localhost:8080/your-websocket-endpoint`).
   - Connect and send messages to test the real-time collaboration features.

## Usage Guidelines

- The `CollaborationEndpoint.java` file manages WebSocket connections and handles incoming messages from clients.
- The `DocumentService.java` file contains the business logic for document management, including methods to create, update, and retrieve documents.
- The `Document.java` file defines the structure of a document, including properties such as title, content, and version.
- The `GsonUtil.java` file provides utility methods for converting Java objects to JSON and vice versa.

## Conclusion

This backend implementation provides the necessary functionality for a real-time collaborative editing experience. For further details on the frontend implementation, please refer to the frontend README file.