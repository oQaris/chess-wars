package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.model.move.Move;

import java.util.List;

public class Human extends Player {
    public Human(Color color) {
        super(color);
    }

    @Override
    public Move move(List<Move> moves) {

        // move

        return null;
    }

    public void lose() {
        // surrender
    }
}
