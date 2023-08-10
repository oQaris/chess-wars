package io.deeplay.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.engine.GameSession;
import io.deeplay.service.UserCommunicationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 6070;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    // private BufferedWriter out;
    // private BufferedReader in;
    private boolean gameStarted;
    private boolean whiteTurn;
    private ObjectMapper mapper;
    private List<ClientHandler> clients = new ArrayList<>();
    private int currentPlayer = 0;
    private GameSession gameSession;

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            clientSocket = null;
            gameStarted = false;
            whiteTurn = true;
            mapper = new ObjectMapper();
            gameSession = UserCommunicationService.getGameSessionInfo();
            gameSession.startGameSession();
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {
    }

    public void start(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }

        System.out.println("Ready to accept");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
            ClientHandler clientHandler = new ClientHandler(clientSocket, this);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    private static String getResponse(String request) {
        return request.toUpperCase();
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private void sendToAllClients(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}