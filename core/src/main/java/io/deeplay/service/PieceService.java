package io.deeplay.service;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.domain.Color;
import io.deeplay.model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

public class PieceService {
    public static final int BOARD_LENGTH = 8;
    public static final int BOARD_HEIGHT = 8;

    public static List<Piece> getPiecesPossibleToMove(Board board, Color color) {
        List<Piece> movablePieces = new ArrayList<>();

        for (int x = 0; x < BOARD_HEIGHT; x++) {
            for (int y = 0; y < BOARD_LENGTH; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));

                if (!piece.getColor().equals(Color.EMPTY) && piece.getColor().equals(color)
                        && !piece.getPossibleMoves(board).isEmpty()) {
                    movablePieces.add(piece);
                }
            }
        }

        return movablePieces;
    }
}
