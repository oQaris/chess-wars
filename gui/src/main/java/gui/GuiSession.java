package gui;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameStates;
import io.deeplay.domain.GameType;
import io.deeplay.engine.GameInfo;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;
import lombok.Getter;

@Getter
public class GuiSession {
    private final Player player1;
    private final Player player2;
    private final GameType gameType;
    private GameInfo gameInfo;

    public GuiSession(Player player1, Player player2, GameType gameType) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameType = gameType;
        this.gameInfo = new GameInfo();
    }

    public void makeMove(Coordinates selectedPiece, Coordinates moveCoordinates) {
        Color currentColor = gameInfo.getCurrentMoveColor();
        Color enemyColor = gameInfo.getCurrentMoveColor().opposite();

        Player playerWhoMoves = choosePlayer(currentColor);

        Move move = playerWhoMoves.getMove(gameInfo.getCurrentBoard(), currentColor, selectedPiece, moveCoordinates);

        gameInfo.move(move);

        if (GameState.isCheck(gameInfo.getCurrentBoard(), currentColor)) {
            endGame("Game ended, because "
                    + currentColor + " is in check and can't move");
//            return;
        }

        if (GameState.isMate(gameInfo.getCurrentBoard(), enemyColor)) {
            endGame("MATE, " + currentColor + " won");
//            return;
        }
        if (GameState.isStaleMate(gameInfo.getCurrentBoard(), enemyColor)) {
            endGame("STALEMATE");
//            return;
        }
        if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(gameInfo.getCurrentBoard())) {
            endGame("DRAW!");
//            return;
        }

        gameInfo.setCurrentMoveColor(gameInfo.getCurrentMoveColor().opposite());

//        return gameInfo.getCurrentBoard();
    }

    public Player choosePlayer(Color movingColor) {
        if (player1.getColor() == movingColor) return player1;
        else return player2;
    }

    public void endGame(String textMessage) {
        System.exit(0);
    }
}