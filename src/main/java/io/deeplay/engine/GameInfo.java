package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.MoveHistory;
import io.deeplay.model.player.Player;

public class GameInfo {

    private Player player1;
    private Player player2;
    private Color currentMoveColor;
    private Board currentBoard;
    private MoveHistory moveHistory;

    public GameInfo(Board currentBoard, Player player1, Player player2) {
        this.currentMoveColor = Color.WHITE;
        this.currentBoard = currentBoard;
        this.moveHistory = new MoveHistory();
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Color getCurrentMoveColor() {
        return currentMoveColor;
    }

    public void setCurrentMoveColor(Color currentMoveColor) {
        this.currentMoveColor = currentMoveColor;
    }

    public Board getCurrentBoardState() {
        return currentBoard;
    }

    public void setCurrentBoardState(Board currentBoardState) {
        this.currentBoard = currentBoardState;
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(MoveHistory moveHistory) {
        this.moveHistory = moveHistory;
    }
}
