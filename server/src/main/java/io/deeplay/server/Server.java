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
    private List<ClientHandler> clients = new ArrayList<>();
    private GameSession gameSession;
    private boolean isGameStarted;
    private GameType gameType;
    Player serverPlayer1;
    Player serverPlayer2;

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

            if (clientHandler.getGameType() == GameType.HumanVsHuman) {
                if (serverPlayer1 == null)
                    serverPlayer1 = new Human(clientHandler.getColor(), new GuiUserCommunicationService());
                else serverPlayer2 = new Human(clientHandler.getColor(), new GuiUserCommunicationService());
            }

            clients.add(clientHandler);
            new Thread(clientHandler).start();
            logger.info("New client connected");

            if (clients.size() == 1) {
                gameType = clientHandler.getGameType();

                if (gameType == GameType.HumanVsHuman) {
                    logger.info("Один игрок подключен, ожидание второго");
                } else if (gameType == GameType.BotVsBot) {
                    String startMessage = "Game bot-bot has started";
                    serverPlayer1 = new Bot(Color.WHITE, 1, new UserCommunicationService(System.in, System.out));
                    serverPlayer2 = new Bot(Color.BLACK, 1, new UserCommunicationService(System.in, System.out));
                    broadcast(startMessage);
                    isGameStarted = true;

                    startGame(); // сделать start
                    break;
                } else if (gameType == GameType.HumanVsBot) {
                    String startMessage = "Game human-bot has started";
                    broadcast(startMessage);

                    serverPlayer1 = new Human(clientHandler.getColor(), new GuiUserCommunicationService());
                    serverPlayer2 = new Bot(clientHandler.getColor().opposite(), 1, new UserCommunicationService(System.in, System.out));

                    isGameStarted = true;
                    startGame(); // сделать start
                    break;
                }
            } else if (clients.size() == 2 && !isGameStarted) {
                String startMessage = "Game human-human has started";
                broadcast(startMessage);
                isGameStarted = true;

                startGame(); // сделать start
                break;
            }
        }
    }

    private void startGame() {
        gameSession = new GameSession(serverPlayer1, serverPlayer2, convertGameTypeDTO(gameType)) {
            @Override
            public void sendMove() {
                if (getGameInfo().getCurrentMoveColor().equals(serverPlayer1.getColor())) {
                    Server.this.sendMove(serverPlayer2.getMove(gameSession.getGameInfo().getCurrentBoard(), gameSession.getGameInfo().getCurrentMoveColor()));
                } else if (getGameInfo().getCurrentMoveColor().equals(serverPlayer2.getColor())) {
                    Server.this.sendMove(serverPlayer1.getMove(gameSession.getGameInfo().getCurrentBoard(), gameSession.getGameInfo().getCurrentMoveColor()));
                }
            }

            // добавить concurrent queue
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

    public void sendMove(Move move) {
        Color color = gameSession.getGameInfo().getCurrentMoveColor();
        if (clients.get(0).getColor().equals(color)) {
            clients.get(1).sendMove((SerializationService.convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
        }

        if (clients.get(1).getColor().equals(color)) {
            clients.get(0).sendMove((SerializationService.convertMoveDTOToJson(Converter.convertMoveToMoveDTO(move))));
        } else {
            logger.error("Не удалось найти ход игрока");
        }
    }

    public void broadcastMove(MoveDTO move) {
        for (ClientHandler client : clients) {
            String serializedMoveDTO = SerializationService.convertMoveDTOToJson(move);
            client.sendMove(serializedMoveDTO); //теперь можно отправить мувы
        }
    }

    public void broadcast(String message) { // сообщения обоим игрокам
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}