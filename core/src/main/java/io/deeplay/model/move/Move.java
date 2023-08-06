package io.deeplay.model.move;

import io.deeplay.domain.MoveType;
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
        return startPosition;
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

    @Override
    public String toString() {
        return "Start position = " + startPosition + " end position + " + endPosition;
    }
}
