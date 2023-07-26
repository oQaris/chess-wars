package io.deeplay.model.player;

import io.deeplay.model.piece.Color;


public abstract class Player {
    protected Color piecesColor;

    public Player(Color piecesColor) {
        this.piecesColor = piecesColor;
    }

    public abstract Move move(List<Move> moves);

    public Color getPiecesColor() {
        return piecesColor;
    }
}
