package io.deeplay.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.engine.GameSession;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveHistory;
import io.deeplay.service.UserCommunicationService;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket clientSocket;
    private Server server;
    BufferedWriter out;
    BufferedReader in;
    private ObjectMapper objectMapper;
    private String player;
    private GameSession gameSession;
    private Board board;
    private MoveHistory moveHistory;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        objectMapper = new ObjectMapper();
        board = new Board();
        moveHistory = new MoveHistory();
    }

    @Override
    public void run() {
        try {
            String clientInput;
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            UserCommunicationService userCommunicationService = new UserCommunicationService(System.in, System.out);
            GameSession gameSession = userCommunicationService.getGameSessionInfo();
            gameSession.startGameSession();

            player = getPlayerName();
            startGame();

            server.broadcast("Player " + player + " had connected");


            // ожидание ходов от клиента
            while ((clientInput = in.readLine()) != null) {
                handleClientInput(clientInput);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientInput(String clientInput) {
        try {
            // Преобразование JSON-строки в объект Move
            Move move = objectMapper.readValue(clientInput, Move.class);

            // Логика обработки ввода клиента, включая ходы игрока, проверки и отправку на сервер
            boolean canMove = validateMove(String.valueOf(move));

            if (canMove) {
                // Преобразование объекта Move обратно в JSON-строку для отправки на сервер
                String moveJson = objectMapper.writeValueAsString(move);

                sendMoveToServer(moveJson); // отправляет на сервер
                String serverResponse = receiveServerResponse(); // получает ответ сервера,
                // логика с получением хода, добавляет историю, обновляет доску и тд
                sendMessageToClient(serverResponse); // отправляет клиенту ответ сервера

                // сервер отправляет запрос на ход другого игрока

                // проверить на завершение игры
                if (server.isGameOver()) {
                    server.broadcast("Game over!");
                    server.resetGame();
                }
            } else {
                sendMessageToClient("Invalid move. Please try again.");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPlayerName() throws IOException {
        sendMessageToClient("Enter your name:");
        return in.readLine();
    }

    public void sendMessage(String message) throws IOException {
        out.write(message);
        out.flush();
        out.newLine();
    }

    public void sendMove(String move) throws IOException {
        String jsonData = objectMapper.writeValueAsString(move);
        out.write(jsonData);
        out.flush();
    }

    private void sendMoveToServer(String move) throws IOException {
        out.write(move);
        out.flush();
    }

    private String receiveServerResponse() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void sendMessageToClient(String message) throws IOException {
        out.write(message);
        out.flush();
    }

    private boolean validateMove(String moveString) throws JsonProcessingException {
        Move move = objectMapper.readValue(moveString, Move.class);

        // Получение текущего состояния доски
        Board board = server.getBoardState();

        // Проверка корректности хода
        return board.getPiece(move.getStartPosition()).canMoveAt(move.getEndPosition(), board);
    }

    private void startGame() throws IOException {
        sendMessageToClient("Are you ready to start the game? (yes/no)");
        String response = in.readLine();
        if (response.equalsIgnoreCase("yes")) {
            // server.startGame(player);
            sendMessageToClient("The game has started!");
        } else {
            sendMessageToClient("Okay, maybe next time.");
            throw new RuntimeException("Player " + player + " declined to start the game.");
        }
    }
}