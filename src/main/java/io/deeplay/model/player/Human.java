package io.deeplay.model.player;

import io.deeplay.model.Board;
import io.deeplay.model.move.Move;

import java.util.List;

public class Human extends Player {
    public Human(char piecesColor) {
        super(piecesColor);
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
