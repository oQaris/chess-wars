package io.deeplay.service;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;

public class MoveService {

    public static Move createMove(Piece selectedPiece, Coordinates moveCoordinates, Board board) {
        return new Move(selectedPiece.getCoordinates(), moveCoordinates, MoveTypeService.getType(selectedPiece, moveCoordinates, board), '\0');
    }
}
