package io.deeplay.client;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.MoveDTO;
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
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private static final Logger logger = LogManager.getLogger(Client.class);
    private final StartGameDTO gameSettings;
    private EndGameDTO endGameDTO;

    public Client(StartGameDTO gameSettings) {
        this.gameSettings = gameSettings;
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

        } catch (IOException e) {
            logger.error("Проблема с подключением к серверу");
        }
    }

    public Object startListening() {
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
            return Converter.convertDTOToMove(DeserializationService.convertJsonToMoveDTO(json));
        } catch (NullPointerException e1) {
            try {
                EndGameDTO endGameDTO = DeserializationService.convertJsonToEndGameDTO(json);

                return Converter.convertEndGameStateDTO(endGameDTO);
            } catch (NullPointerException e2) {
                logger.error("wrong type DTO");
            }
        }

        throw new NullPointerException("wrong type DTO");
//        Gson gson = new Gson();
//        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
//
//        if (jsonObject.has("endGameStateType")) {
//            EndGameDTO endGameDTO = DeserializationService.convertJsonToEndGameDTO(json);
//            return endGameDTO;
//            // обработать конец игры
//        } else if (jsonObject.has("exception")) {
//            ErrorResponseDTO errorResponseDTO = DeserializationService.convertJsonToErrorResponseDTO(json);
//            return errorResponseDTO;
//            // обработать ошибку
//        } else if (jsonObject.has("startPosition")) {
//            MoveDTO moveDTO = DeserializationService.convertJsonToMoveDTO(json);
//            Move move = receiveMove(moveDTO);
//            return move;
//            // обработка хода
//        } else if (jsonObject.has("currentMoveColor")) {
//            MoveTransferDTO moveTransferDTO = DeserializationService.convertJsonToMoveTransferDTO(json);
//            return moveTransferDTO;
//            // обработать moveTransferDTO
//        } else if (jsonObject.has("botLevel")) {
//            StartGameDTO startGameDTO = DeserializationService.convertJsonToStartGameDTO(json);
//            return startGameDTO;
//        } else {
//            throw new JsonSyntaxException("Тип Json не найден");
//        }
    }

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

    public Move receiveMove(MoveDTO moveDTO) {
        return Converter.convertDTOToMove(moveDTO);
    }

    public Move getMove() {
        return null;
    }
}