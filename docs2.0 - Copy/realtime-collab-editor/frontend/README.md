# Frontend Real-Time Collaborative Editor

This project is a real-time collaborative editor built using Java for the backend and HTML, CSS, and JavaScript for the frontend. It allows multiple users to edit documents simultaneously, providing a seamless collaborative experience.

## Project Structure

The frontend of the application is organized as follows:

- **public/**: Contains the main HTML, CSS, and JavaScript files.
  - **index.html**: The entry point of the web application.
  - **styles/**: Contains CSS files for styling the application.
    - **main.css**: The main stylesheet for the application.
  - **scripts/**: Contains JavaScript files for application logic.
    - **app.js**: The main JavaScript file handling user interactions and WebSocket connections.

- **package.json**: The npm configuration file that lists the dependencies and scripts for the frontend application.

## Setup Instructions

1. **Clone the Repository**: 
   ```bash
   git clone <repository-url>
   cd realtime-collab-editor/frontend
   ```

2. **Install Dependencies**: 
   Make sure you have Node.js installed. Then run:
   ```bash
   npm install
   ```

3. **Run the Application**: 
   You can use a local server to serve the files. For example, you can use `lite-server` or any other static server:
   ```bash
   npm start
   ```

4. **Access the Application**: 
   Open your web browser and navigate to `http://localhost:3000` (or the port specified by your server).

## Features

- Real-time collaboration on documents.
- User-friendly interface for editing and managing documents.
- WebSocket connection for real-time updates.

## Testing WebSocket Connection

You can test the WebSocket connection using Postman:

1. Open Postman and create a new WebSocket request.
2. Enter the WebSocket URL (e.g., `ws://localhost:8080/collaborate`).
3. Connect and send messages to test the real-time collaboration feature.

## Acknowledgments

This project utilizes various libraries and frameworks to enhance functionality and user experience. Special thanks to the contributors and the open-source community for their support.