package io.deeplay.model.player;

import io.deeplay.model.Board;

public class Human extends Player {
    public Human(char piecesColor) {
        super(piecesColor);
    }

    @Override
    public Board move(Board board) {

        // move

        return board;
    }

    public void lose() {
        // surrender
    }
}
