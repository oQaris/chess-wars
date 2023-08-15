package io.deeplay.model.move;

import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.model.Coordinates;

public record Move(Coordinates startPosition, Coordinates endPosition, MoveType moveType,
                   SwitchPieceType switchPieceType) {

    @Override
    public String toString() {
        return "Start position = " + startPosition + " end position + " + endPosition;
    }
}
