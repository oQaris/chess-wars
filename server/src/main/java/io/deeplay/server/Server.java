package io.deeplay.server;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.model.GameType;
import io.deeplay.communication.service.SerializationService;
import io.deeplay.domain.Color;
import io.deeplay.engine.GameSession;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;
import io.deeplay.service.GuiUserCommunicationService;
import io.deeplay.service.UserCommunicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static io.deeplay.communication.converter.Converter.convertGameTypeDTO;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private boolean isGameStarted;
    private GameType gameType;
    private Player serverPlayer1;
    private Player serverPlayer2;

    public Server(int port) {
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
            System.out.println("Client " + clients.size() + 1 + " connected");

            ClientHandler clientHandler = new ClientHandler(socket, this);

            gameType = clientHandler.getGameType();
            switch (clientHandler.getGameType()) {
                case HumanVsHuman -> {
                    if (serverPlayer1 == null) {
                        serverPlayer1 = new Human(clientHandler.getColor(), new GuiUserCommunicationService());
                    } else {
                        serverPlayer2 = new Human(clientHandler.getColor(), new GuiUserCommunicationService());
                    }
                }
                case HumanVsBot -> {
                    if (serverPlayer1 == null) {
                        serverPlayer1 = new Human(clientHandler.getColor(), new GuiUserCommunicationService());
                    } else {
                        serverPlayer2 = new Bot(clientHandler.getColor(), clientHandler.getBotLevel(), new GuiUserCommunicationService());
                    }
                }
                case BotVsBot -> {
                    if (serverPlayer1 == null) {
                        serverPlayer1 = new Bot(clientHandler.getColor(), clientHandler.getBotLevel(), new GuiUserCommunicationService());
                    } else {
                        serverPlayer2 = new Bot(clientHandler.getColor(), clientHandler.getBotLevel(), new GuiUserCommunicationService());
                    }
                }
                default -> throw new IllegalArgumentException("Тип игры не был распознан");
            }

            clients.add(clientHandler);
            new Thread(clientHandler).start();
            logger.info("New client connected");

            if (clients.size() == 2 && !isGameStarted) {
                isGameStarted = true;

                startGame();
                break;
            }
        }
    }

    private void startGame() {
        GameSession gameSession = new GameSession(serverPlayer1, serverPlayer2, convertGameTypeDTO(gameType)) {
            public void sendMove(Move move) {
                Color moveColor = getGameInfo().getCurrentMoveColor();
                if (moveColor.equals(serverPlayer1.getColor())) {
                    if (clients.get(0).getColor().equals(moveColor)) {
                        clients.get(1).sendMoveToClient((SerializationService
                                .convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
                        System.out.println("We sent move to other client: " + clients.get(1).getColor());
                    }
                    if (clients.get(1).getColor().equals(moveColor)) {
                        clients.get(0).sendMoveToClient((SerializationService
                                .convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
                        System.out.println("We sent move to other client: " + clients.get(0).getColor());
                    } else logger.error("Не удалось найти ход игрока");
                } else if (getGameInfo().getCurrentMoveColor().equals(serverPlayer2.getColor())) {
                    if (clients.get(0).getColor().equals(moveColor)) {
                        clients.get(1).sendMoveToClient((SerializationService
                                .convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
                    }
                    if (clients.get(1).getColor().equals(moveColor)) {
                        clients.get(0).sendMoveToClient((SerializationService
                                .convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
                    } else logger.error("Не удалось найти ход игрока");
                }
            }

            @Override
            public Move getMove(Player player, Color color) {
                if (clients.get(0).getColor().equals(color)) {
                    try {
                        return clients.get(0).getMove();
                    } catch (IOException e) {
                        logger.error("cannot get move");
                    }
                }

                if (clients.get(1).getColor().equals(color)) {
                    try {
                        return clients.get(1).getMove();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                throw new IllegalStateException();
            }
        };

        gameSession.startGameSession();
    }

    public void broadcast(String message) { // сообщения обоим игрокам
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}