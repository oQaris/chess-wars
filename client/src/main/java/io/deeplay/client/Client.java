package io.deeplay.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.*;
import io.deeplay.communication.model.Color;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.DeserializationService;
import io.deeplay.communication.service.SerializationService;
import io.deeplay.model.move.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private static final Logger logger = LogManager.getLogger(Client.class);
    private StartGameDTO gameSettings;

    public Client(List<String> gameSettings) {
        this.gameSettings = getStartGameSettings(gameSettings);
    }

    private StartGameDTO getStartGameSettings(List<String> gameSettings) {
        GameType clientGameType;
        Color clientColor;
        int clientBotLevel;

        switch (gameSettings.get(0)) {
            case "Человек vs. Человек" -> clientGameType = GameType.HumanVsHuman;
            case "Человек vs. Бот" -> clientGameType = GameType.HumanVsBot;
            case "Бот vs. Бот" -> clientGameType = GameType.BotVsBot;
            default -> throw new IllegalArgumentException("Wrong Game Type selection");
        }

        switch (gameSettings.get(1)) {
            case "Белый" -> clientColor = Color.WHITE;
            case "Черный" -> clientColor = Color.BLACK;
            default -> throw new IllegalArgumentException("Wrong Color selection");
        }

        switch (gameSettings.get(2)) {
            case "Легкий" -> clientBotLevel = 1;
            case "Средний" -> clientBotLevel = 2;
            case "Сложный" -> clientBotLevel = 3;
            default -> throw new IllegalArgumentException("Wrong Bot Level selection");
        }

        return new StartGameDTO(clientGameType, clientColor, clientBotLevel);
    }

    public void connectToServer() {
        try {
            socket = new Socket(HOST, PORT);
            logger.info("Клиент подключился к серверу");
            System.out.println("Connected to server");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String response = SerializationService.convertStartGameDTOtoJSON(gameSettings);
            out.write(response);
            out.newLine();
            out.flush();

            startListening();
        } catch (IOException e) {
            logger.error("Проблема с подключением к серверу");
        }
//        finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void processJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        if (jsonObject.has("endGameStateType")) {
            EndGameDTO endGameDTO = DeserializationService.convertJsonToEndGameDTO(json);
            // обработать конец игры
        } else if (jsonObject.has("exception")) {
            ErrorResponseDTO errorResponseDTO = DeserializationService.convertJsonToErrorResponseDTO(json);
            // обработать ошибку
        } else if (jsonObject.has("startPosition")) {
            MoveDTO moveDTO = DeserializationService.convertJsonToMoveDTO(json);
            Move move = receiveMove(moveDTO);
            // обработка хода
        } else if (jsonObject.has("currentMoveColor")) {
            MoveTransferDTO moveTransferDTO = DeserializationService.convertJsonToMoveTransferDTO(json);
            // обработать moveTransferDTO
        } else if (jsonObject.has("botLevel")) {
            StartGameDTO startGameDTO = DeserializationService.convertJsonToStartGameDTO(json);
        } else {
            throw new JsonSyntaxException("Тип Json не найден");
        }
    }

    public void startListening() throws IOException {
//        String request = in.readLine();
    }

    public void sendMove(Move move) {
        String moveJson = SerializationService.convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move));
        try {
            out.write(moveJson);
            out.flush();
        } catch (IOException e) {
            logger.error("Ошибка отправки хода от клиента");
            throw new RuntimeException(e);
        }
    }

    public Move receiveMove(MoveDTO moveDTO) {
        return Converter.convertDTOToMove(moveDTO);
    }

    public Move getMove() {
        return null;
    }

    public static void main(String[] args) {
//        Client client = new Client();
//        client.connectToServer();
        // gui.getMove();
    }
}