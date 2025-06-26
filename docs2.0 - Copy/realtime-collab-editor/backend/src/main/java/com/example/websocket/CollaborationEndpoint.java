package com.example.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/collab")
public class CollaborationEndpoint {

    private static final Set<CollaborationEndpoint> connections = new CopyOnWriteArraySet<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connections.add(this);
        System.out.println("New connection: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from client: " + message);
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        connections.remove(this);
        System.out.println("Connection closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error on connection: " + session.getId() + " - " + throwable.getMessage());
    }

    private void broadcast(String message) {
        for (CollaborationEndpoint client : connections) {
            try {
                client.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                System.err.println("Error sending message to client: " + e.getMessage());
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException ex) {
                    System.err.println("Error closing session: " + ex.getMessage());
                }
            }
        }
    }
}