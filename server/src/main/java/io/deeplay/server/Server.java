package io.deeplay.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.engine.GameSession;
import io.deeplay.model.move.Move;
import io.deeplay.service.UserCommunicationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    ;
    private boolean gameStarted;
    private boolean whiteTurn;
    private ObjectMapper mapper;
    private List<ClientHandler> clients = new ArrayList<>();
    private int currentPlayer = 0;
    private GameSession gameSession;

    public Server() {
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) throws IOException {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        Server server = new Server();
        server.start(2);
    }

    public void start(int playersNumber) throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }

        System.out.println("Ready to accept");

        while (clients.size() < playersNumber) {
            Socket socket = serverSocket.accept();
            System.out.println("Client " + (clients.size() + 1) + " connected");

            ClientHandler clientHandler = new ClientHandler(socket, this);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
            System.out.println("New client connected");
        }

        // send startGame (всем игрокам)

        // старт и логика
        gameSession = UserCommunicationService.getGameSessionInfo();
        gameSession.startGameSession();
    }

    private static String getResponse(String response) { // дессериализует response
        // в зависимости от внутренности выполняет определенные методы (switch)
        return response.toUpperCase();
    }

    public void broadcastMove(Move move) throws IOException {
        for (ClientHandler client : clients) {
            client.sendMove(move);
        }
    }

    public void broadcast(String message) throws IOException {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private synchronized void startGame() throws IOException { //  synchronized (???)
        gameStarted = true;

        for (ClientHandler client : clients) {
            client.sendMessage("{\"start\": true}");
        }
    }

    private synchronized void endGame() throws IOException { //  synchronized (???)
        gameStarted = false;

        for (ClientHandler client : clients) {
            client.sendMessage("{\"end\": true}");
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}