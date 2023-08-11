package io.deeplay.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.DeserializationService;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    Socket clientSocket;
    private final Server server;
    BufferedWriter out;
    BufferedReader in;
    Board board;
    GameType gameType;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        board = new Board();
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        gameType = chooseGameType();
    }

    @Override
    public void run() {
        try {
            handleGameStart(gameType);

            while (true) {
                // serverPlayer.setMove(move); // передать игроку???
            }

        } catch (Exception e) {
            logger.error("Ошибка при подключении игрока: ", e);
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Ошибка при прочтении данных: ", e);
                e.printStackTrace();
            }
        }
    }

    public Move getMove() throws IOException {
        String clientInput = in.readLine();
        MoveDTO moveDTO = DeserializationService.convertJsonToMoveDTO(clientInput);
        return Converter.convertDTOToMove(moveDTO);
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
        out.flush();out.newLine();
        } catch (IOException e) {
            logger.error("Не получилось отправить сообщение ", e);
        }
    }

//    public void sendMove(String move) {
//        String jsonData;
//
//        try {
//            jsonData = objectMapper.writeValueAsString(move);
//            out.write(jsonData);
//            out.flush();
//        } catch (JsonProcessingException e) {
//            logger.error("Не получилось преобразовать данные ", e);
//        } catch (IOException e) {
//            logger.error("Не получилось отправить данные на сервер ", e);
//        }
//    }

    private void sendMoveToServer(String move) throws IOException {
        out.write(move);
        out.flush();
    }

    /**
     * Отправляет сообщение клиенту.
     *
     * @param message сообщение для отправки
     */
    void sendMessageToClient(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Не получилось отправить сообщение клиенту: ", e);
            e.printStackTrace();
        }
    }

    /**
     * Проверяет корректность хода.
     *
     * @param moveString строка с ходом
     * @return true, если ход корректный, false в противном случае
     * * @throws JsonProcessingException
     */
    private boolean validateMove(String moveString) {
        Move move = null;

        try {
            MoveDTO moveDTO = DeserializationService.convertJsonToMoveDTO(moveString);
            move = Converter.convertDTOToMove(moveDTO);
        } catch (JsonProcessingException e) {
            logger.error("Не получилось обработать ход: ", e);
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Получение текущего состояния доски
        Board board = server.getBoardState();

        // Проверка корректности хода
        return board.getPiece(move.startPosition()).canMoveAt(move.endPosition(), board);
    }

    private GameType chooseGameType() throws IOException {
        sendMessageToClient("Choose game type: bot-bot / human-bot / human-human");
        String response = in.readLine();

        if (response.equalsIgnoreCase("bot-bot")) {
            sendMessageToClient("Welcome to the chess bot-bot");
            gameType = GameType.BotVsBot;
        } else if (response.equalsIgnoreCase("human-bot")) {
            sendMessageToClient("Welcome to the chess human-bot");
            gameType = GameType.HumanVsBot;
        } else if (response.equalsIgnoreCase("human-human")) {
            sendMessageToClient("Waiting for player 2");
            gameType = GameType.HumanVsHuman;
        } else {
            logger.info("Игрок не согласился на игру");
        }

        return gameType;
    }

    private void handleGameStart(GameType gameType) { // обработать запрос на старт игры
    }

    public GameType getGameType() {
        return gameType;
    }
}