package com.editor;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.CopyOnWriteArraySet;
import com.google.gson.*;

public class EditorWebSocketHandler extends TextWebSocketHandler {
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private static final Gson gson = new Gson();
    private static String documentContent = "";
    private static int version = 0;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

        // Send connection status
        JsonObject statusMsg = new JsonObject();
        statusMsg.addProperty("type", "connection");
        statusMsg.addProperty("status", "connected");
        session.sendMessage(new TextMessage(gson.toJson(statusMsg)));

        // Send current content
        JsonObject contentMsg = new JsonObject();
        contentMsg.addProperty("type", "content");
        contentMsg.addProperty("content", documentContent);
        contentMsg.addProperty("version", version);
        session.sendMessage(new TextMessage(gson.toJson(contentMsg)));

        broadcastUserCount();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        String type = jsonMessage.get("type").getAsString();

        if ("change".equals(type)) {
            documentContent = jsonMessage.get("content").getAsString();
            version++;

            JsonObject responseMsg = new JsonObject();
            responseMsg.addProperty("type", "change");
            responseMsg.addProperty("content", documentContent);
            responseMsg.addProperty("version", version);
            String responseString = gson.toJson(responseMsg);

            // Echo to sender
            session.sendMessage(new TextMessage(responseString));

            // Broadcast to others
            for (WebSocketSession s : sessions) {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    s.sendMessage(new TextMessage(responseString));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        broadcastUserCount();
    }

    private void broadcastUserCount() {
        JsonObject countMsg = new JsonObject();
        countMsg.addProperty("type", "users");
        countMsg.addProperty("count", sessions.size());
        String countString = gson.toJson(countMsg);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(countString));
                } catch (Exception ignored) {}
            }
        }
    }
}