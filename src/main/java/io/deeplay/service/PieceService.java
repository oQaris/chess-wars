package io.deeplay.service;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Color;
import io.deeplay.model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

public class PieceService {
    public static List<Piece> getPiecesPossibleToMove(Board board) {
        List<Piece> movablePieces = new ArrayList<>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));
                if (!piece.getColor().equals(Color.EMPTY) && !piece.getPossibleMoves(board).isEmpty()) {
                    movablePieces.add(piece);
                }
            }
        }

        return movablePieces;
    }
}
