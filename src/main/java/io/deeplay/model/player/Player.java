package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.model.move.Move;

import java.util.List;

public abstract class Player {
    protected Color color;

    public Player(Color color) {
        this.color = color;
    }

    public abstract Move move(List<Move> moves);

    public Color getColor() {
        return color;
    }
}
