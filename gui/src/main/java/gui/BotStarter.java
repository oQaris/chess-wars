package gui;

import gui.service.GamePropertiesService;
import io.deeplay.client.Client;
import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameInfo;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Player;
import io.deeplay.service.GuiUserCommunicationService;
import io.deeplay.service.UserCommunicationService;

import java.util.List;

public class BotStarter {
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

        this.player = new Bot(currentColor, startGameDTO.getBotLevel(), new GuiUserCommunicationService());
        this.client = new Client(startGameDTO);
        client.connectToServer();

        if (!isPlayerMove) {
            waitAndUpdate();
        }
    }

    public void startBot() {
        while (true) {
            if (isPlayerMove) {
                Move move = player.getMove(gameInfo.getCurrentBoard(), player.getColor());

                gameInfo.move(move);
                client.sendMove(move);
                isPlayerMove = false;
            }

            waitAndUpdate();
        }
    }

    public void waitAndUpdate() {
        Move move = (Move) client.startListening();
        updateGameInfo(move);
    }

    public void updateGameInfo(Move incomingMove) {
        gameInfo.move(incomingMove);
        isPlayerMove = true;
    }
}
