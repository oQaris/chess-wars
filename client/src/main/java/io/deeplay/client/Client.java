package io.deeplay.client;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.service.DeserializationService;
import io.deeplay.model.move.Move;
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
    private static final Logger logger = LogManager.getLogger(Client.class);

    public Client(String serverAddress, int serverPort) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void connectToServer() {
        try {
            Client client = new Client(SERVER_IP, PORT);
            logger.info("Клиент подключился к серверу");
            System.out.println("Connected to server");

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

    public void sendMove(Move move) {
        //   MoveDTO moveDTO = Converter.convertMoveToDTO(move);
        //  String moveJson = SerializationService.convertMoveDTOToJson(moveDTO);

        try {
            //       out.write(moveJson);
            // out.write(moveDTO) ??
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Move getMove() {
        Move move;

        try {
            String json = in.readLine();
            MoveDTO moveDTO = DeserializationService.convertJsonToMoveDTO(json);
            move = Converter.convertDTOToMove(moveDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return move;
    }

    public static void main(String[] args) throws IOException {
        // gui.getMove();
    }
}