package io.deeplay.server;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.SerializationService;
import io.deeplay.engine.GameSession;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.player.Player;
import io.deeplay.service.UserCommunicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private ObjectMapper mapper;
    private List<ClientHandler> clients = new ArrayList<>();
    private GameSession gameSession;
    private boolean isGameStarted;
    private boolean gameStarted = false;
    private DeserializationContext objectMapper;
    private Player player1;
    private Player player2;
    private GameType gameType;

    public Server(int port) {
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) throws IOException {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        Server server = new Server(PORT);
        server.start();
    }

    public void start() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("Server started on port " + PORT);
        } catch (IOException e) {
            logger.error("Could not listen on port: " + PORT);
            System.exit(1);
        }

        logger.info("Ready to accept");

        while (true) {
            Socket socket = serverSocket.accept();
            logger.info("Client {} connected", clients.size() + 1);

            ClientHandler clientHandler = new ClientHandler(socket, this);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
            logger.info("New client connected");

            if (clients.size() == 1) {
                gameType = clientHandler.getGameType();

                if (gameType == GameType.HumanVsHuman) {
                    String waitMessage = "Waiting for second player";
                    broadcast(waitMessage);
                    logger.info("Один игрок подключен, ожидание второго");
                } else if (gameType == GameType.BotVsBot) {
                    String startMessage = "Game bot-bot has started";
                    broadcast(startMessage);
                    isGameStarted = true;

                    gameSession = new UserCommunicationService(System.in, System.out).getGameSessionInfo();
                    gameSession.startGameSession();

                    String startMessage = "The game has started!";
        broadcast(startMessage);

        String whiteTurnMessage = "White pieces move";
        broadcast(whiteTurnMessage);

        broadcastMove(new MoveDTO(new Move(
                new Coordinates(1, 1), new Coordinates(1, 3), MoveType.ORDINARY, SwitchPieceType.NULL)));

        // startGame(); ??
                    break;
                } else if (gameType == GameType.HumanVsBot) {
                    String startMessage = "Game human-bot has started";

                    broadcast(startMessage);
                    isGameStarted = true;
                    startGame();
                    break;
                }
            } else if (clients.size() == 2 && !isGameStarted) {
                String startMessage = "Game human-human has started";
                broadcast(startMessage);
                isGameStarted = true;
                gameSession.startGameSession();
                startGame();
                break;
            }
        }
    }

    private void startGame() {
        // GameSession gameSession1 = new GameSession(player1, player2, gameType); // TODO: спросить Серёжу про gameType
        // TODO: спросить про DTO и core
    }

    public void broadcastMove(MoveDTO move) {
        for (ClientHandler client : clients) {
            String serializedMoveDTO = SerializationService.makeMoveDTOToJson(move);
            client.sendMove(serializedMoveDTO);
        }
    }

    public synchronized void broadcast(String message) { // сообщения обоим игрокам
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private String receiveClientResponse(ClientHandler client) {
        try {
            return client.in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void endGame() {
        gameStarted = false;

        for (ClientHandler client : clients) {
            client.sendMessage("{\"end\": true}");
        }
    }

    public void createNewGame(StartGameDTO startGameDTO) {
        // создание новой игры
        // gameSession = new GameSession(); // TODO: передать Player ов и тип игры

        // отправка сообщения о начале игры всем клиентам
        String startGameMessage = "Game started!";
        broadcast(startGameMessage);
    }

    public void handleEndGame(ClientHandler clientHandler) {
        // обработка конца игры
        EndGameDTO endGameDto = new EndGameDTO();
        Gson gson = new Gson();
        String json = gson.toJson(endGameDto);

        for (ClientHandler client : clients) {
            client.sendMessageToClient(json);
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public Board getBoardState() {
        return null;
    }

    public boolean isGameOver() {
        return false;
    }

    public void resetGame() {
    }

    public void broadcastGameState(GameState gameState) {
        // Рассылает состояние игры всем клиентам
    }
}