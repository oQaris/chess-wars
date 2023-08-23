package io.deeplay.server;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.DeserializationService;
import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    private final ConcurrentLinkedQueue<Move> movesQueue;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        board = new Board();
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        movesQueue = new ConcurrentLinkedQueue<>();
        startGameDTO = getStartGame();
        gameType = startGameDTO.getGameType();
        color = Converter.convertColor(startGameDTO.getCurrentColor());
        botLevel = startGameDTO.getBotLevel();

        System.out.println(gameType);
    }

    @Override
    public void run() {
        try {
            while (true) {
                // serverPlayer.setMove(move); // передать игроку???
                Move move = getMove();
                movesQueue.offer(move);
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

    public StartGameDTO getStartGame() throws IOException{
        String clientInput = in.readLine();
        return DeserializationService.convertJsonToStartGameDTO(clientInput);
    }

    public Move getMove() throws IOException {
        String clientInput = in.readLine();
        MoveDTO moveDTO = DeserializationService.convertJsonToMoveDTO(clientInput);
        return Converter.convertDTOToMove(moveDTO);
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.flush();
            out.newLine();
        } catch (IOException e) {
            logger.error("Не получилось отправить сообщение ", e);
        }
    }

//    public void sendMove(String serializedMoveDTO) {
//        try {
//            out.write(serializedMoveDTO);
//            out.newLine();
//            out.flush();
//        } catch (IOException e) {
//            logger.error("Не получилось отправить ход: ", e);
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

    private void handleGameStart(GameType gameType) { // обработать запрос на старт игры
    }
}