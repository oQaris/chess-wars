package io.deeplay.marinaAI.strategy;

import io.deeplay.domain.Color;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.*;

public class MaterialStrategy implements Strategy {
    Color maximizingColor;

    public MaterialStrategy(Color maximizingColor){
        this.maximizingColor = maximizingColor;
    }

    @Override
    public int evaluate(Board board) {
        if (GameState.isMate(board, maximizingColor.opposite())) {
            return 100000000;
        }

        if (GameState.isStaleMate(board, maximizingColor.opposite())) {
            return 0;
        }

        if (GameState.isMate(board, maximizingColor)) {
            return -100000000;
        }

        if (GameState.isStaleMate(board, maximizingColor)) {
            return 0;
        }

        if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)) {
            return 0;
        }

        return getMaterialScore(board);
    }

    /**
     Метод getMaterialScore вычисляет оценку материального баланса на шахматной доске.
     @param board         шахматная доска, для которой нужно вычислить оценку
     @return оценка материального баланса на доске
     */
    public int getMaterialScore(Board board) {
        int finalScore = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(new Coordinates(i, j));

                if (piece.getColor() == maximizingColor) {
                    finalScore += getPieceScore(piece);
                } else if (piece.getColor() == maximizingColor.opposite()) {
                    finalScore -= getPieceScore(piece);
                }
            }
        }

        return finalScore;
    }


    /**
     Метод getPieceScore вычисляет оценку для конкретной фигуры на шахматной доске.
     @param piece  фигура, для которой нужно вычислить оценку
     @return оценка для данной фигуры
     */
    private int getPieceScore(Piece piece) {
        if (piece instanceof Pawn) {
            return 10;
        } else if (piece instanceof Knight || piece instanceof Bishop) {
            return 30;
        } else if (piece instanceof Rook) {
            return 50;
        } else if (piece instanceof Queen) {
            return 90;
        } else if (piece instanceof King) {
            return 900;
        }

        return 0;
    }
}
