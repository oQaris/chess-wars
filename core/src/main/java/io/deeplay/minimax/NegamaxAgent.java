package io.deeplay.minimax;

import io.deeplay.domain.Color;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import io.deeplay.service.BoardUtil;

import java.util.List;

public class NegamaxAgent extends AbstractAiAgent {
    private Color maximizingColor;
    public Move getBestMove(Board board, int depth, int alpha, int beta, Color currentColor) {
        maximizingColor = currentColor;
        return (Move) negamax(board, depth, alpha, beta, currentColor)[0];
    }

    private Object[] negamax(Board board, int depth, int alpha, int beta, Color currentColor) {
        if (depth == 0
                || GameState.isMate(board, currentColor)
                || GameState.isStaleMate(board, currentColor)
                || GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)) {
            return new Object[]{null, calculatePieces(board, currentColor)};
        }

        List<Move> allPossibleMoves = getAllPossibleMoves(board, currentColor);
        Move bestMove = getRandomMove(allPossibleMoves);
        int bestScore = Integer.MIN_VALUE;

        for (Move move : allPossibleMoves) {
            Board duplicateBoard = new Board();
            BoardUtil.duplicateBoard(board).accept(duplicateBoard);
            duplicateBoard.move(move);

            int score = -(int) negamax(duplicateBoard, depth - 1, -beta, -alpha, currentColor.opposite())[1];

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return new Object[]{bestMove, bestScore};
    }

    int calculatePieces(Board board, Color currentColor) {
        if (GameState.isMate(board, currentColor)) {
            if (currentColor == maximizingColor) return 800000;
            else return -800000;
        }

        if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(board) || GameState.isStaleMate(board, currentColor)) {
            return 0;
        }

        int finalScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece curPiece = board.getPiece(new Coordinates(i, j));
                if (!(curPiece instanceof Empty)) {
                    if (curPiece.getColor() == currentColor) {
                        if (curPiece instanceof Pawn) finalScore += 10;
                        else if (curPiece instanceof Knight || curPiece instanceof Bishop) finalScore += 30;
                        else if (curPiece instanceof Rook) finalScore += 50;
                        else if (curPiece instanceof Queen) finalScore += 90;
                        else if (curPiece instanceof King) finalScore += 900;
                    } else if (curPiece.getColor() == currentColor.opposite()){
                        if (curPiece instanceof Pawn) finalScore -= 10;
                        else if (curPiece instanceof Knight || curPiece instanceof Bishop) finalScore -= 30;
                        else if (curPiece instanceof Rook) finalScore -= 50;
                        else if (curPiece instanceof Queen) finalScore -= 90;
                        else if (curPiece instanceof King) finalScore -= 900;
                    }
                }
            }
        }

        return finalScore;
    }
}
