package io.deeplay.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.MoveTransferDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.GameType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 8080;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private boolean isMyTurn = false;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(Client.class);

    public void connectToServer() {
        try {
            socket = new Socket(SERVER_IP, PORT);
            logger.info("Клиент подключился к серверу");
            System.out.println("Connected to server");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            objectMapper = new ObjectMapper();
            String response = in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMove(MoveDTO move) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(move);

        out.write(json);
        out.newLine();
        out.flush();
    }

    public void sendGameStartRequest(GameType gameType) {
        StartGameDTO gameStartDTO = new StartGameDTO(gameType);
        Gson gson = new Gson();
        String json = gson.toJson(gameStartDTO);

        try {
            out.write(json);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleMoveTransfer(MoveTransferDTO moveTransferDTO) { // обработка полученного хода

    }

    public void handleEndGame(EndGameDTO endGameDto) { // обработка сообщения о конце игры
    }


    public String receiveMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String receiveError() {
        return null;
    }

    public void disconnect() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
    }
}