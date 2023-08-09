package io.deeplay.model.move;

import io.deeplay.domain.MoveType;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Piece;

public record Move(Coordinates startPosition, Coordinates endPosition, MoveType moveType) {
    @Override
    public String toString() {
        return "Start position = " + startPosition + " end position + " + endPosition;
    }
}