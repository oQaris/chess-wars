package io.deeplay.model.player;

public abstract class Player {
    protected char piecesColor;

    public Player(char piecesColor) {
        this.piecesColor = piecesColor;
    }

    public abstract void move();

    public char getPiecesColor() {
        return piecesColor;
    }
}
