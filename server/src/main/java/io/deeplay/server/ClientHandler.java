package io.deeplay.server;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.GameStateType;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.DeserializationService;
import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    Socket clientSocket;
    private final Server server;
    BufferedWriter out;
    BufferedReader in;
    Board board;
    private StartGameDTO startGameDTO;
    @Getter
    private GameType gameType;
    @Getter
    private Color color;
    @Getter
    private int botLevel;
    @Setter
    @Getter
    private Player player;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        board = new Board();
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        startGameDTO = getStartGame();
        System.out.println(startGameDTO.toString());
        gameType = startGameDTO.getGameType();
        color = Converter.convertColor(startGameDTO.getCurrentColor());
        botLevel = startGameDTO.getBotLevel();
    }

    @Override
    public void run() {
        try {
            while (true) {

            }
        } catch (Exception e) {
            logger.error("Ошибка при подключении игрока: ", e);
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Ошибка при закрытии сокета: ", e);
            }
        }
    }

    public StartGameDTO getStartGame() {
        String clientInput;

        try {
            clientInput = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return DeserializationService.convertJsonToStartGameDTO(clientInput);
    }

//    public EndGameDTO getEndGame() {
//        String clientInput;
//
//        try {
//            clientInput = in.readLine();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return DeserializationService.convertJsonToEndGameDTO(clientInput);
//    }

    public Move getMove() throws IOException {
        String clientInput = in.readLine();
        MoveDTO moveDTO = DeserializationService.convertJsonToMoveDTO(clientInput);
        System.out.println("received move:" + moveDTO);
        Move move = Converter.convertDTOToMove(moveDTO);
        System.out.println("getMove in run method");
        return move;
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Не получилось отправить сообщение ", e);
        }
    }

    public void sendMoveToClient(String serializedMoveDTO) {
        try {
            out.write(serializedMoveDTO);
            out.newLine();
            out.flush();
            System.out.println("Sent move to client from a server!");
        } catch (IOException e) {
            logger.error("Не получилось отправить ход: ", e);
        }
    }

    public void sendEndGameToClient(String serializedEndGameDTO) {
        try {
            out.write(serializedEndGameDTO);
            out.newLine();
            out.flush();
            System.out.println(serializedEndGameDTO);
            System.out.println("Sent end game to client from a server!");
        } catch (IOException e) {
            logger.error("Не получилось отправить ход: ", e);
        }
    }

    public EndGameDTO getEndGame(List<String> gameEnd) {
        GameStateType endGameStateType;
        io.deeplay.communication.model.Color winColor;

        switch (gameEnd.get(0)) {
            case "CHECK" -> endGameStateType = GameStateType.CHECK;
            case "CHECKMATE" -> endGameStateType = GameStateType.CHECKMATE;
            case "STALEMATE" -> endGameStateType = GameStateType.STALEMATE;
            case "DRAW" -> endGameStateType = GameStateType.DRAW;
            default -> throw new IllegalArgumentException("Wrong game ending");
        }

        switch (gameEnd.get(1)) {
            case "WHITE" -> winColor = io.deeplay.communication.model.Color.WHITE;
            case "BLACK" -> winColor = io.deeplay.communication.model.Color.BLACK;
            default -> throw new IllegalArgumentException("Wrong Color");
        }

        return new EndGameDTO(endGameStateType, winColor);
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

    private void handleGameStart(GameType gameType) { // обработать запрос на старт игры
    }
}