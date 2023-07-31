package io.deeplay.service;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.MoveType;
import io.deeplay.model.piece.Piece;

public class MoveTypeService {
    public static MoveType getType(Piece selectedPiece, Coordinates moveCoordinates, Board board) {

        return MoveType.ORDINARY;
    }
}
