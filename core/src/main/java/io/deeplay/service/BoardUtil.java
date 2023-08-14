package io.deeplay.service;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;

import java.util.function.Consumer;

public class BoardUtil {

    public static Consumer<Board> duplicateBoard(Board board) {
        return duplicateBoard -> {
            if (board == null) throw new IllegalStateException();

            duplicateBoard.setBoard(new Piece[8][8]);

            if (board.getMoveHistory().getLastMove() != null) {
                Move lastMove = board.getMoveHistory().getLastMove();
                duplicateBoard.getMoveHistory().addMove(lastMove);
            }

            Piece piece;
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    piece = board.getPiece(new Coordinates(x,y));
                    duplicateBoard.setPiece(new Coordinates(x, y), piece);
                }
            }
        };
    }
}
