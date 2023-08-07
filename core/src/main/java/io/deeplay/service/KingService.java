package io.deeplay.service;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Piece;

import java.util.List;

public class KingService {
    private static final int HEIGHT = 8;
    private static final int WIDTH = 8;
    public static boolean isPossibleToCastle(Coordinates kingCoordinates, List<Coordinates> potentialCoordinates,
                                             Board board, Color enemyColor) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (board.getPiece(new Coordinates(i,j)).getColor().equals(enemyColor)) {
                    Piece piece = board.getPiece(new Coordinates(i,j));

                    if (piece.canMoveAt(kingCoordinates, board)) return false;

                    for (Coordinates potentialCoordinate : potentialCoordinates) {
                        if (piece.canMoveAt(potentialCoordinate, board))
                            return false;
                    }
                }
            }
        }
        return true;
    }
}
