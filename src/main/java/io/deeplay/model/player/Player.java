package io.deeplay.model.player;

import io.deeplay.model.Board;
import io.deeplay.model.move.Move;

import java.util.List;

public abstract class Player {
    protected char piecesColor;

    public Player(char piecesColor) {
        this.piecesColor = piecesColor;
    }

    public abstract Move move(List<Move> moves);

    public char getPiecesColor() {
        return piecesColor;
    }
}
