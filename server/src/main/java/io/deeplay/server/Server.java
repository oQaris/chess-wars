package io.deeplay.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.MoveDTO;
import io.deeplay.engine.GameSession;
import io.deeplay.model.Board;
import io.deeplay.service.UserCommunicationService;
import service.SerializationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private ObjectMapper mapper;
    private List<ClientHandler> clients = new ArrayList<>();
    private GameSession gameSession;
    private boolean isGameStarted;
    private boolean gameStarted;

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

        if (clients.size() == 2) {
            String startMessage = "The game has started!";

            broadcast(startMessage);
            isGameStarted = true;

            UserCommunicationService userCommunicationService = new UserCommunicationService(System.in, System.out);
            gameSession = userCommunicationService.getGameSessionInfo();
            gameSession.startGameSession();
        }

//        broadcastMove(new MoveDTO(new Move(
//                new Coordinates(1, 1), new Coordinates(1, 3), MoveType.ORDINARY, SwitchPieceType.NULL)));
    }


    private static String getResponse(String response) { // десериализует response
        // в зависимости от внутренности выполняет определенные методы (switch)
        return response.toUpperCase();
    }

    public void broadcastMove(MoveDTO move) throws IOException {
        for (ClientHandler client : clients) {
            String serializedMoveDTO = SerializationService.makeMoveDTOToJson(move);
            client.sendMove(serializedMoveDTO);
        }
    }

    public synchronized void broadcast(String message) throws IOException { // сообщения обоим игрокам
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private String receiveClientResponse(ClientHandler client) {
        try {
            return client.in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private synchronized void endGame() throws IOException { //  synchronized (???)
        gameStarted = false;

        for (ClientHandler client : clients) {
            client.sendMessage("{\"end\": true}");
        }
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }


    public Board getBoardState() { 
        return null;
    }

    public boolean isGameOver() {
        return false;
    }

    public void resetGame() {
    }
}