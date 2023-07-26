package io.deeplay.model;

public abstract class Player {
    protected char piecesColor;

    public Player(char piecesColor) {
        this.piecesColor = piecesColor;
    }

    public abstract void move();

    public String getPiecesColor() {
        return piecesColor;
    }
}
