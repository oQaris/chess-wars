package gui;

import io.deeplay.client.Client;
import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.domain.Color;
import io.deeplay.engine.GameInfo;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.MinimaxBot;
import io.deeplay.model.player.Player;
import io.deeplay.service.GuiUserCommunicationService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BotStarter implements EndpointUser {
    private boolean isPlayerMove;
    private final GameInfo gameInfo;
    private final Player player;
    private final Client client;
    public BotStarter(StartGameDTO startGameDTO) {
        Color currentColor = Converter.convertColor(startGameDTO.getCurrentColor());
        isPlayerMove = currentColor == io.deeplay.domain.Color.WHITE;
        this.gameInfo = new GameInfo(currentColor) {
            @Override
            protected void changeCurrentMoveColor() {

            }
        };

        this.player = new MinimaxBot(currentColor, startGameDTO.getBotLevel(), new GuiUserCommunicationService());
        this.client = new Client(startGameDTO);
        client.connectToServer();

        if (!isPlayerMove) {
            waitAndUpdate();
        }
    }

    public void initialize() {
        while (true) {
            if (isPlayerMove) {
                Move move = player.getMove(gameInfo.getCurrentBoard(), player.getColor());
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                gameInfo.move(move);
                client.sendMove(move);
                isPlayerMove = false;
            }

            waitAndUpdate();
        }
    }

    public void endGame(List<String> endGameInfo) {

    }

    public void waitAndUpdate() {
//        new Thread(() -> {
//            Object playerAction = client.startListening();
//
//            if (playerAction instanceof Move move) {
//                updateGameInfo(move);
//            } else if (playerAction instanceof List<?>) {
//                System.out.println("game over in wait and update");
//
//                List<String> endGameInfo = (List<String>) playerAction;
//                endGame(endGameInfo);
//            }
//
//        }).start();
        Move move = (Move) client.startListening();
        updateGameInfo(move);
    }

    public void updateGameInfo(Move incomingMove) {
        gameInfo.move(incomingMove);
        isPlayerMove = true;
    }
}
