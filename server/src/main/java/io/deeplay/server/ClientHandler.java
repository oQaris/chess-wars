package io.deeplay.server;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.ErrorResponseDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.DeserializationService;
import io.deeplay.domain.BotType;
import io.deeplay.domain.Color;
import io.deeplay.model.Board;
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
    private BotType botType;
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
        System.out.println(startGameDTO.getBotType());
        botType = startGameDTO.getBotType();
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

    public Object listenJson() {
        String request;

        try {
            request = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return processJson(request);
    }

    public Object processJson(String json) {
        try {
            EndGameDTO endGameDTO = DeserializationService.convertJsonToEndGameDTO(json);

            return Converter.convertEndGameStateDTO(endGameDTO);
        } catch (NullPointerException e1) {
            try {
                return Converter.convertDTOToMove(DeserializationService.convertJsonToMoveDTO(json));
            } catch (NullPointerException e2) {
                logger.error("wrong type DTO");
            }
        }

        throw new NullPointerException("wrong type DTO");
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
        } catch (IOException e) {
            logger.error("Не получилось отправить ход: ", e);
        }
    }

    public void sendErrorToClient(String serializedErrorDTO) {
        try {
            out.write(serializedErrorDTO);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Не получилось отправить ошибку: ", e);
        }
    }

    public EndGameDTO getEndGame(List<String> gameEnd) {
        return Converter.convertListEndGameToEndGameDTO(gameEnd);
    }

    public ErrorResponseDTO getError(Exception exception, String error) {
        return Converter.convertErrorToErrorResponseDTO(exception, error);
    }
}