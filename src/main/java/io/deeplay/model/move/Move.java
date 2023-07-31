package io.deeplay.model.move;

import io.deeplay.model.Coordinates;

public class Move {
    private Coordinates startPosition;
    private Coordinates endPosition;
    private MoveType moveType;
    private char switchTo;

    public Move(Coordinates startPosition, Coordinates endPosition, MoveType moveType, char switchTo) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.moveType = moveType;
        this.switchTo = switchTo;
    }

    public Coordinates getStartPosition() {
        return endPosition;
    }

    public Coordinates getEndPosition() {
        return endPosition;
    }

    public char getSwitchTo() {
        return switchTo;
    }

    public MoveType getMoveType() {
        return moveType;
    }
}