package io.deeplay.model.move;

import io.deeplay.domain.MoveType;
import io.deeplay.model.Coordinates;

public class Move {
    private final Coordinates startPosition;
    private final Coordinates endPosition;
    private final MoveType moveType;

    public Move(Coordinates startPosition, Coordinates endPosition, MoveType moveType) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.moveType = moveType;
    }

    public Coordinates getStartPosition() {
        return startPosition;
    }

    public Coordinates getEndPosition() {
        return endPosition;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    @Override
    public String toString() {
        return "Start position = " + startPosition + " end position + " + endPosition;
    }
}
