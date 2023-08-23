package io.deeplay.client;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.Color;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.DeserializationService;
import io.deeplay.communication.service.SerializationService;
import io.deeplay.model.move.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client implements Runnable {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private static final Logger logger = LogManager.getLogger(Client.class);
    private StartGameDTO gameSettings;

    public Client(List<String> gameSettings) {
        this.gameSettings = getStartGameSettings(gameSettings);
        connectToServer();
    }

    private StartGameDTO getStartGameSettings(List<String> gameSettings) {
        GameType clientGameType;
        Color clientColor;
        Integer clientBotLevel;

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

            String request = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void sendMove(Move move) {
        String moveJson = SerializationService.convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move));
        try {
            out.write(moveJson);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Move getMove(String serverMove) { // с сервака берем ход
        return Converter.convertDTOToMove(DeserializationService.convertJsonToMoveDTO(serverMove));
    }

    public static void main(String[] args) {
//        Client client = new Client();
//        client.connectToServer();
       // gui.getMove();
    }

    @Override
    public void run() {

    }
}