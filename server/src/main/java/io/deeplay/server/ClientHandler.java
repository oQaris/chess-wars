package io.deeplay.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.model.move.Move;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket clientSocket;
    private Server server;
    private BufferedWriter out;
    private BufferedReader in;
    private ObjectMapper objectMapper;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try {
            String clientInput;
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // ожидание ходов от клиента, обработка ходов, отправка обновлений клиентам
            while ((clientInput = in.readLine()) != null) {
                handleClientInput(clientInput);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientInput(String clientInput) {
        try {
            // Преобразование JSON-строки в объект Move
            Move move = objectMapper.readValue(clientInput, Move.class);

            // Логика обработки ввода клиента, включая ходы игрока, проверки и отправку на сервер
            boolean canMove = validateMove(String.valueOf(move));

            if (canMove) {
                // Преобразование объекта Move обратно в JSON-строку для отправки на сервер
                String moveJson = objectMapper.writeValueAsString(move);

                sendMoveToServer(moveJson); // отправляет на сервер
                String serverResponse = receiveServerResponse(); // получает ответ сервера,
                // логика с получением хода, добавляет историю, обновляет доску и тд
                sendMessageToClient(serverResponse); // отправляет клиенту ответ сервера

                // сервер отправляет запрос на ход другого игрока

                // проверить на завершение игры
            } else {
                sendMessageToClient("Invalid move. Please try again.");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) throws IOException {
        out.write(message);
        out.flush();
    }

    public void sendMove(Move move) throws IOException {
        String jsonData = objectMapper.writeValueAsString(move);
        out.write(jsonData);
        out.flush();
    }

    private void sendMoveToServer(String move) throws IOException {
        out.write(move);
        out.flush();
    }

    private String receiveServerResponse() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendMessageToClient(String message) throws IOException {
        out.write(message);
        out.flush();
    }

    private boolean validateMove(String move) {
        // логика проверки хода
        return true;
    }
}