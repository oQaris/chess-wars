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
    BufferedWriter out;
    BufferedReader in;
    Board board;
    @Getter
    private final GameType gameType;
    @Getter
    private final Color color;
    @Getter
    private final BotType botType;
    @Setter
    @Getter
    private Player player;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        board = new Board();
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        StartGameDTO startGameDTO = getStartGame();
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

    /**
     * Метод getStartGame возвращает объект типа StartGameDTO, который представляет собой настройки игры,
     * полученные от клиента.
     *
     * @return объект типа StartGameDTO, содержащий настройки игры
     * @throws RuntimeException если возникла ошибка при чтении ввода от клиента
     */
    public StartGameDTO getStartGame() {
        String clientInput;

        try {
            clientInput = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return DeserializationService.convertJsonToStartGameDTO(clientInput);
    }

    /**
     * Метод listenJson слушает входящий JSON-запрос от клиента и передает его на обработку.
     *
     * @return объект, полученный в результате обработки JSON-запроса
     * @throws RuntimeException если возникла ошибка при чтении ввода от клиента
     */
    public Object listenJson() {
        String request;

        try {
            request = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return processJson(request);
    }

    /**
     * Метод processJson обрабатывает входящий JSON-запрос и возвращает соответствующий объект.
     *
     * @param json строка, содержащая JSON-запрос
     * @return объект, полученный в результате обработки JSON-запроса
     * @throws NullPointerException если передан неверный тип объекта в JSON-запросе
     */
    public Object processJson(String json) {
        try {
            EndGameDTO endGameDTO = DeserializationService.convertJsonToEndGameDTO(json);

            return Converter.convertEndGameStateDTO(endGameDTO);
        } catch (NullPointerException e1) {
            try {
                return Converter.convertDTOToMove(DeserializationService.convertJsonToMoveDTO(json));
            } catch (NullPointerException e2) {
                try {
                    ErrorResponseDTO errorResponseDTO = DeserializationService.convertJsonToErrorResponseDTO(json);

                    return Converter.convertErrorResponseDTOToList(errorResponseDTO);
                } catch (NullPointerException e3) {
                    logger.error("wrong type DTO");
                }
            }
        }

        throw new NullPointerException("wrong type DTO");
    }

    /**
     * Метод sendMoveToClient отправляет сериализованный объект типа MoveDTO клиенту.
     *
     * @param serializedMoveDTO строка, содержащая сериализованный объект типа MoveDTO
     */
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

    /**
     * Метод sendEndGameToClient отправляет сериализованный объект типа EndGameDTO клиенту.
     *
     * @param serializedEndGameDTO строка, содержащая сериализованный объект типа EndGameDTO
     */
    public void sendEndGameToClient(String serializedEndGameDTO) {
        try {
            out.write(serializedEndGameDTO);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Не получилось отправить ход: ", e);
        }
    }

    /**
     * Метод sendErrorToClient отправляет сериализованный объект типа ErrorDTO клиенту.
     *
     * @param serializedErrorDTO строка, содержащая сериализованный объект типа ErrorDTO
     */
    public void sendErrorToClient(String serializedErrorDTO) {
        try {
            out.write(serializedErrorDTO);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Не получилось отправить ошибку: ", e);
        }
    }

    /**
     * Метод getEndGame преобразует список строк gameEnd в объект типа EndGameDTO.
     *
     * @param gameEnd список строк, содержащий информацию об окончании игры
     * @return объект типа EndGameDTO, соответствующий переданным данным об окончании игры
     */
    public EndGameDTO getEndGame(List<String> gameEnd) {
        return Converter.convertListEndGameToEndGameDTO(gameEnd);
    }

    /**
     * Метод getError преобразует исключение и сообщение об ошибке в объект типа ErrorResponseDTO.
     *
     * @param exception исключение, которое произошло
     * @param error     сообщение об ошибке
     * @return объект типа ErrorResponseDTO, содержащий информацию об ошибке
     */
    public ErrorResponseDTO getError(Exception exception, String error) {
        return Converter.convertErrorToErrorResponseDTO(exception, error);
    }
}