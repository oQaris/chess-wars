package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;

import java.util.List;

public abstract class Player {
    protected Color color;

    public Player(Color color) {
        this.color = color;
    }

    public abstract Move move(List<Piece> possiblePiecesToMove, Board board);

    public Color getColor() {
        return color;
    }
}
