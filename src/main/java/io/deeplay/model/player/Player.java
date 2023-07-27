package io.deeplay.model.player;

import io.deeplay.model.Board;

public abstract class Player {
    protected char piecesColor;

    public Player(char piecesColor) {
        this.piecesColor = piecesColor;
    }

    public abstract Board move(Board board);

    public char getPiecesColor() {
        return piecesColor;
    }
}
