package com.editor;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@ServerEndpoint("/collaborative-editor/ws")
public class WebSocketServer {
    private static final Set<Session> userSessions = Collections.synchronizedSet(new HashSet<>());
    private static final Gson gson = new Gson();
    private static String currentContent = "";

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
        contentMsg.addProperty("content", currentContent);
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

            JsonObject jsonMessage = gson.fromJson(message, JsonObject.class);
            String type = jsonMessage.get("type").getAsString();

            if ("change".equals(type)) {
                currentContent = jsonMessage.get("content").getAsString();
                System.out.println("\n=== Content Updated ===");
                System.out.println("New content: " + currentContent);

                JsonObject broadcastMsg = new JsonObject();
                broadcastMsg.addProperty("type", "change");
                broadcastMsg.addProperty("content", currentContent);
                String broadcastString = gson.toJson(broadcastMsg);

                System.out.println("\n=== Broadcasting Changes ===");
                synchronized(userSessions) {
                    for (Session client : userSessions) {
                        if (!client.equals(session)) {
                            try {
                                client.getBasicRemote().sendText(broadcastString);
                                System.out.println("Sent to client: " + client.getId());
                            } catch (IOException e) {
                                System.err.println("Failed to send to client " + client.getId() + ": " + e.getMessage());
                            }
                        }
                    }
                }
                System.out.println("Broadcasting complete");
            }
        } catch (Exception e) {
            System.err.println("\n=== Error Processing Message ===");
            System.err.println("Error: " + e.getMessage());
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
                if (session.isOpen() && !session.equals(senderSession)) {
                    try {
                        session.getBasicRemote().sendText(message);
                        System.out.println("Sent to session: " + session.getId());
                    } catch (IOException e) {
                        System.err.println("Failed to send to " + session.getId() + ": " + e.getMessage());
                    }
                }
            }
        }
        System.out.println("Broadcasting complete");
    }

    private void sendMessage(Session session, String message) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message);
                System.out.println("Message sent successfully to: " + session.getId());
            }
        } catch (IOException e) {
            System.err.println("Error sending message to " + session.getId() + ": " + e.getMessage());
        }
    }
}