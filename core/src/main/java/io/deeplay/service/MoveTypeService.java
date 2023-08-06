package io.deeplay.service;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.domain.MoveType;
import io.deeplay.model.piece.Empty;
import io.deeplay.model.piece.Pawn;
import io.deeplay.model.piece.Piece;

public class MoveTypeService {
    public static MoveType getType(Piece selectedPiece, Coordinates moveCoordinates, Board board) {
        if (selectedPiece instanceof Pawn) {
            if (((Pawn) selectedPiece).isPromotion(moveCoordinates, board)) {
                return MoveType.PROMOTION;
            }
        }

        if (selectedPiece instanceof Pawn) {
            if (((Pawn) selectedPiece).isEnPassant(moveCoordinates, board)) {
                return MoveType.EN_PASSANT;
            }
        }

        if (!(board.getPiece(moveCoordinates) instanceof Empty)) {
            return MoveType.TAKE;
        }

        return MoveType.ORDINARY;
    }
}
