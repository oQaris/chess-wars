package io.deeplay.server;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.service.SerializationService;
import io.deeplay.domain.Color;
import io.deeplay.engine.GameSession;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveHistory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private ObjectMapper mapper;
    private List<ClientHandler> clients = new ArrayList<>();
    private int currentPlayer = 0;
    private GameSession gameSession;
    private boolean isGameStarted;
    private boolean gameStarted = false;
    private DeserializationContext objectMapper;

    public Server(int port) {
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) throws IOException {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        Server server = new Server(PORT);
        server.start(2);
    }

    public void start(int playersNumber) throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("Server started on port " + PORT);
        } catch (IOException e) {
            logger.error("Could not listen on port: " + PORT);
            System.exit(1);
        }

        logger.info("Ready to accept");

        while (clients.size() < playersNumber) {
            Socket socket = serverSocket.accept();
            logger.info("Client {} connected", clients.size() + 1);

            ClientHandler clientHandler = new ClientHandler(socket, this);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
            logger.info("New client connected");
        }

        if (clients.size() == 2 && !isGameStarted) {
            String startMessage = "The game has started!";

            broadcast(startMessage);
            isGameStarted = true;

            startGame();
//            UserCommunicationService userCommunicationService = new UserCommunicationService(System.in, System.out);
//            gameSession = userCommunicationService.getGameSessionInfo();
//            gameSession.startGameSession();
        }

//        broadcastMove(new MoveDTO(new Move(
//                new Coordinates(1, 1), new Coordinates(1, 3), MoveType.ORDINARY, SwitchPieceType.NULL)));
    }


    private static String getResponse(String response) { // десериализует response
        // в зависимости от внутренности выполняет определенные методы (switch)
        return response.toUpperCase();
    }

    public void broadcastMove(MoveDTO move) {
        for (ClientHandler client : clients) {
            String serializedMoveDTO = SerializationService.convertMoveDTOToJson(move);
            client.sendMove(serializedMoveDTO);
        }
    }

    public synchronized void broadcast(String message){ // сообщения обоим игрокам
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

    private synchronized void endGame() { //  synchronized (???)
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

    void startGame() {
        boolean isWhiteTurn = true;
        for (ClientHandler client : clients) {
            if (isWhiteTurn) {
                client.sendMessageToClient("You are white");
                client.sendMessageToClient("It's your turn!");
            } else {
                client.sendMessageToClient("You are black");
                client.sendMessageToClient("Waiting for opponent's move");
            }

            isWhiteTurn = !isWhiteTurn;
        }

        // Игровой цикл
        Board board = new Board();
        MoveHistory history = new MoveHistory();

        while (true) {
            // Ожидание хода от игрока
            ClientHandler currentPlayer = null;
            ClientHandler opponentPlayer = null;
            for (ClientHandler client : clients) {
                if ((client.getPlayerColor() == Color.WHITE && isWhiteTurn) ||
                        (client.getPlayerColor() == Color.BLACK && !isWhiteTurn)) {
                    currentPlayer = client;
                } else {
                    opponentPlayer = client;
                }
            }

            Move move = currentPlayer.receiveMoveFromClient();

            // Проверка возможности совершения хода
            if (!board.getPiece(move.startPosition()).canMoveAt(move.endPosition(), board)) {
                currentPlayer.sendMessageToClient("Invalid move. Please try again.");
                continue;
            }

            // Обновление доски и истории
            board.move(move);
            history.addMove(move);

            // Проверка на завершение игры
//            if () {
//                for (ClientHandler client : clients) {
//                    client.sendMessageToClient("Game over!");
//                }
//                System.exit(0);
//            }

            // Отправка хода оппоненту
            String moveJson = objectMapper.toString();
            opponentPlayer.sendMessageToClient(moveJson);

            // Смена хода
            isWhiteTurn = !isWhiteTurn;
            currentPlayer.sendMessageToClient("Waiting for opponent's move");
            opponentPlayer.sendMessageToClient("It's your turn!");
        }
    }

    public void broadcastGameState(GameState gameState) {
        // Рассылает состояние игры всем клиентам
    }
}