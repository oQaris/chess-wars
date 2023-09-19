package io.deeplay.client;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.service.DeserializationService;
import io.deeplay.communication.service.SerializationService;
import io.deeplay.model.move.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private BufferedWriter out;
    private BufferedReader in;
    private static final Logger logger = LogManager.getLogger(Client.class);
    private final StartGameDTO gameSettings;

    public Client(StartGameDTO gameSettings) {
        this.gameSettings = gameSettings;
    }

    /**
     * Метод подключается к серверу по заданному хосту и порту. Затем отправляет запрос на начало игры серверу.
     */
    public void connectToServer() {
        try {
            Socket socket = new Socket(HOST, PORT);
            logger.info("Клиент подключился к серверу");
            System.out.println("Connected to server");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String response = SerializationService.convertStartGameDTOtoJSON(gameSettings);
            out.write(response);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Проблема с подключением к серверу");
        }
    }

    /**
     * Метод нужен для запуска "прослушивания". Ждет входящий json запрос.
     * @return обработанный объект, полученный из json
     */
    public Object startListening() {
        String request;

        try {
            request = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return processJson(request);
    }

    /**
     * Обрабатывает приходящий json запрос.
     * @param json строка запроса
     * @return обработанный объект, полученный из json
     */
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

    /**
     * Отправляет сериализованный ход на сервер
     * @param move ход игрока
     */
    public void sendMove(Move move) {
        String moveJson = SerializationService.convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move));

        try {
            out.write(moveJson);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Ошибка отправки хода от клиента");
            throw new RuntimeException(e);
        }
    }
}