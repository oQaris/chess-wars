package io.deeplay.minimax;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import io.deeplay.model.player.Player;
import io.deeplay.service.BoardUtil;

import java.util.*;

import static io.deeplay.model.player.Player.getPiecesPossibleToMove;
import static io.deeplay.model.player.Player.getType;

public class MinimaxAgent extends AbstractAiAgent {
    private Color maximizingColor;
    private Color minimizingColor;

    public Move getBestMove(Board board, int depth, double alpha, double beta, Color currentColor) {
        maximizingColor = currentColor;
        minimizingColor = currentColor.opposite();
        return (Move) minimax(board, depth, alpha, beta, currentColor, true)[0];
    }

    public Object[] minimax(Board board, int depth, double alpha, double beta, Color currentColor, boolean maximizingPlayer) {
        if (depth == 0
                || GameState.isMate(board, currentColor)
                || GameState.isStaleMate(board, currentColor)
                || GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)) {
            return new Object[]{null, calculatePieces(board, currentColor)};
        }

        if (maximizingPlayer) {
            List<Move> allPossibleMoves = getAllPossibleMoves(board, maximizingColor);
            Move currentBestMove = getRandomMove(allPossibleMoves);
            double maxEval = Double.MIN_VALUE;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                double currentEval = (double) minimax(duplicateBoard, depth - 1, alpha, beta, currentColor.opposite(), false)[1];

                if (currentEval > maxEval) {
                    maxEval = currentEval;
                    currentBestMove = move;
                }

                alpha = Math.max(alpha, currentEval);
                if (beta <= alpha) {
                    break;
                }
            }
            return new Object[]{currentBestMove, maxEval};
        } else {
            List<Move> allPossibleMoves = getAllPossibleMoves(board, minimizingColor);
            Move currentBestMove = getRandomMove(allPossibleMoves);
            double minEval = Double.MAX_VALUE;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                double currentEval = (double) minimax(duplicateBoard, depth - 1, alpha, beta, currentColor.opposite(), true)[1];

                if (currentEval < minEval) {
                    minEval = currentEval;
                    currentBestMove = move;
                }

                beta = Math.min(beta, currentEval);
                if (beta <= alpha) {
                    break;
                }
            }
            return new Object[]{currentBestMove, minEval};
        }
    }

    double calculatePieces(Board board, Color currentColor) {
        if (GameState.isMate(board, currentColor)) {
            if (currentColor == maximizingColor) {
                return -8000000;
            } else if (currentColor == minimizingColor) {
                return 8000000;
            }
        } else if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(board) || GameState.isStaleMate(board, currentColor)) {
            return 0;
        }

        double finalScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece curPiece = board.getPiece(new Coordinates(i, j));
                if (!(curPiece instanceof Empty)) {
                    if (curPiece.getColor() == maximizingColor) {
                        if (curPiece instanceof Pawn) finalScore += 10;
                        else if (curPiece instanceof Knight || curPiece instanceof Bishop) finalScore += 30;
                        else if (curPiece instanceof Rook) finalScore += 50;
                        else if (curPiece instanceof Queen) finalScore += 90;
                        else if (curPiece instanceof King) finalScore += 900;
                    } else if (curPiece.getColor() == minimizingColor){
                        if (curPiece instanceof Pawn) finalScore -= 10;
                        else if (curPiece instanceof Knight || curPiece instanceof Bishop) finalScore -= 30;
                        else if (curPiece instanceof Rook) finalScore -= 50;
                        else if (curPiece instanceof Queen) finalScore -= 90;
                        else if (curPiece instanceof King) finalScore -= 900;
                    }
                }
            }
        }
        // TODO тест на то, что на depth = 2 съедает фигуру
        return finalScore;
    }
}
