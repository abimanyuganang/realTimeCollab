package com.editor;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.editor.model.Document;

@ServerEndpoint("/collaborative-editor/ws")
public class WebSocketServer {
    private static final Set<Session> userSessions = Collections.synchronizedSet(new HashSet<>());
    private static final Gson gson = new Gson();
    private static final Document document = new Document();  // Replace currentContent with Document

    @OnOpen
    public void onOpen(Session session) {
        userSessions.add(session);
        System.out.println("\n=== WebSocket Connection Opened ===");
        System.out.println("Session ID: " + session.getId());
        System.out.println("Total active users: " + userSessions.size());
        
        // Send connection status
        JsonObject statusMsg = new JsonObject();
        statusMsg.addProperty("type", "connection");
        statusMsg.addProperty("status", "connected");
        sendMessage(session, gson.toJson(statusMsg));
        System.out.println("Sent connection status to: " + session.getId());
        
        // Send current content
        JsonObject contentMsg = new JsonObject();
        contentMsg.addProperty("type", "content");
        contentMsg.addProperty("content", document.getContent());
        contentMsg.addProperty("version", document.getVersion());
        sendMessage(session, gson.toJson(contentMsg));
        System.out.println("Sent current content to: " + session.getId());
        
        broadcastUserCount();
    }

    @OnClose
    public void onClose(Session session) {
        userSessions.remove(session);
        System.out.println("\n=== WebSocket Connection Closed ===");
        System.out.println("Session ID: " + session.getId());
        System.out.println("Remaining users: " + userSessions.size());
        broadcastUserCount();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            System.out.println("\n=== Message Received ===");
            System.out.println("From session: " + session.getId());
            System.out.println("Raw message: " + message);

            // Add debug logging for active sessions
            synchronized (userSessions) {
                System.out.println("Active sessions before broadcast: " + userSessions.size());
                for (Session s : userSessions) {
                    System.out.println("Session ID: " + s.getId() + ", Open: " + s.isOpen());
                }
            }

            JsonObject jsonMessage = gson.fromJson(message, JsonObject.class);
            String type = jsonMessage.get("type").getAsString();

            if ("change".equals(type)) {
                String content = jsonMessage.get("content").getAsString();
                document.setContent(content);

                // Create response message
                JsonObject responseMsg = new JsonObject();
                responseMsg.addProperty("type", "change");
                responseMsg.addProperty("content", content);
                responseMsg.addProperty("version", document.getVersion());
                String responseString = gson.toJson(responseMsg);

                // Immediate echo back to sender for confirmation
                sendMessage(session, responseString);

                // Broadcast to others using the synchronized broadcastMessage method
                broadcastMessage(responseString, session);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("\n=== WebSocket Error ===");
        System.err.println("Session ID: " + session.getId());
        System.err.println("Error: " + throwable.getMessage());
        userSessions.remove(session);
        broadcastUserCount();
    }

    private void broadcastUserCount() {
        JsonObject countMsg = new JsonObject();
        countMsg.addProperty("type", "users");
        countMsg.addProperty("count", userSessions.size());
        String countString = gson.toJson(countMsg);
        
        System.out.println("\n=== Broadcasting User Count ===");
        System.out.println("Count message: " + countString);
        broadcastMessage(countString, null);
    }

    private void broadcastMessage(String message, Session senderSession) {
        System.out.println("\n=== Broadcasting Message ===");
        synchronized (userSessions) {
            for (Session session : userSessions) {
                if (session.isOpen() && (senderSession == null || !session.equals(senderSession))) {
                    try {
                        session.getBasicRemote().sendText(message);
                        System.out.println("Sent to session: " + session.getId());
                    } catch (IOException e) {
                        System.err.println("Failed to send to " + session.getId() + ": " + e.getMessage());
                        // Consider removing failed sessions
                        userSessions.remove(session);
                    }
                }
            }
        }
        System.out.println("Broadcasting complete");
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
            System.out.println("Message sent to session: " + session.getId());
            System.out.println("Message content: " + message);
        } catch (IOException e) {
            System.err.println("Failed to send message to " + session.getId());
            e.printStackTrace();
        }
    }
}