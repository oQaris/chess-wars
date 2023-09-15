package io.deeplay.server;

import io.deeplay.communication.model.GameType;
import io.deeplay.marinaAI.bot.MiniMaxBot;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;
import io.deeplay.service.GuiUserCommunicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final int PORT = 8080;
    public static final ExecutorService GAMES = Executors.newFixedThreadPool(10);
    private final List<ClientHandler> humanHumanClientList = new LinkedList<>();
    private final List<ClientHandler> humanBotClientList = new LinkedList<>();
    private final List<ClientHandler> botBotClientList = new LinkedList<>();
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
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
                        clientHandler.setPlayer(serverPlayer1);
                        humanHumanClientList.add(clientHandler);
                    } else {
                        serverPlayer2 = new Human(clientHandler.getColor(), new GuiUserCommunicationService());
                        clientHandler.setPlayer(serverPlayer2);
                        humanHumanClientList.add(clientHandler);
                    }
                }
                case HumanVsBot -> {
                    if (serverPlayer1 == null) {
                        serverPlayer1 = new Human(clientHandler.getColor(), new GuiUserCommunicationService());
                        clientHandler.setPlayer(serverPlayer1);
                        humanBotClientList.add(clientHandler);
                    } else {
                        serverPlayer2 = new MiniMaxBot(clientHandler.getColor(), clientHandler.getBotLevel(),
                                new GuiUserCommunicationService());
                        clientHandler.setPlayer(serverPlayer2);
                        humanBotClientList.add(clientHandler);
                    }
                }
                case BotVsBot -> {
                    if (serverPlayer1 == null) {
                        serverPlayer1 = new MiniMaxBot(clientHandler.getColor(), clientHandler.getBotLevel(),
                                new GuiUserCommunicationService());
                        clientHandler.setPlayer(serverPlayer1);
                        botBotClientList.add(clientHandler);
                    } else {
                        serverPlayer2 = new Bot(clientHandler.getColor(), clientHandler.getBotLevel(),
                                new GuiUserCommunicationService());
                        clientHandler.setPlayer(serverPlayer2);
                        botBotClientList.add(clientHandler);
                    }
                }
                default -> throw new IllegalArgumentException("Тип игры не был распознан");
            }

            clients.add(clientHandler);
            new Thread(clientHandler).start();
            logger.info("New client connected");

            if (botBotClientList.size() > 1) {
                botBotClientList.retainAll(tryStartingGame(botBotClientList));
            } else if (humanBotClientList.size() > 1) {
                humanBotClientList.retainAll(tryStartingGame(humanBotClientList));
            } else if (humanHumanClientList.size() > 1) {
                humanHumanClientList.retainAll(tryStartingGame(humanHumanClientList));
            }
        }
    }

    private List<ClientHandler> tryStartingGame(List<ClientHandler> clientHandlerList) {
        if (clientHandlerList.size() > 1) {
            for (int i = 0; i < clientHandlerList.size(); i++) {
                ClientHandler tempCH = clientHandlerList.get(i);
                for (int j = i + 1; j < clientHandlerList.size(); j++) {
                    ClientHandler tempCH2 = clientHandlerList.get(j);
                    if (tempCH.getColor() != tempCH2.getColor()) {
                        List<ClientHandler> startGameClientHandlerList =
                                new ArrayList<>(Arrays.asList(tempCH, tempCH2));
                        GAMES.execute(new GameStarter(startGameClientHandlerList, gameType));
                        clientHandlerList.remove(tempCH);
                        clientHandlerList.remove(tempCH2);
                        gameType = null;
                    }
                }
            }
        }

        return clientHandlerList;
    }

    public void broadcast(String message) { // сообщения обоим игрокам
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}