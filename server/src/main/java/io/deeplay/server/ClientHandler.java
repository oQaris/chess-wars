package io.deeplay.server;

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
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    Socket clientSocket;
    private final Server server;
    BufferedWriter out;
    BufferedReader in;
    Board board;
    GameType gameType;
    private final ConcurrentLinkedQueue<Move> movesQueue;


    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        board = new Board();
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        gameType = chooseGameType();
        movesQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
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

    public void sendMove(String serializedMoveDTO) {
        try {
            out.write(serializedMoveDTO);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Не получилось отправить ход: ", e);
        }
    }

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

    public GameType getGameType() {
        return gameType;
    }
}