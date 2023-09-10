package io.deeplay.ai_agent;

import io.deeplay.domain.Color;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import io.deeplay.service.BoardUtil;

import java.util.List;

public class ExpectiminimaxAgent  extends AbstractAiAgent {
    private Color maximizingColor;
    private Color expectingColor;

    public Move getBestMove(Board board, int depth, Color currentColor) {
        maximizingColor = currentColor;
        expectingColor = currentColor.opposite();
        return (Move) expectimax(board, depth, currentColor, true, true)[0];
    }

    public Object[] expectimax(Board board, int depth, Color currentColor, boolean maximizingPlayer, boolean isChanceNode) {
        if (depth == 0
                || GameState.isMate(board, currentColor)
                || GameState.isStaleMate(board, currentColor)
                || GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)) {
            return new Object[]{null, calculatePieces(board, currentColor)};
        }

        if (maximizingPlayer && isChanceNode) {
            List<Move> allPossibleMoves = getAllPossibleMoves(board, maximizingColor);
            Move currentBestMove = getRandomMove(allPossibleMoves);
            int maxEval = Integer.MIN_VALUE;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                int currentEval = (int) expectimax(duplicateBoard, depth - 1, currentColor.opposite(), false, false)[1];

                if (currentEval > maxEval) {
                    maxEval = currentEval;
                    currentBestMove = move;
                }
            }
            return new Object[]{currentBestMove, maxEval};
        } else if (maximizingPlayer) {
            List<Move> allPossibleMoves = getAllPossibleMoves(board, maximizingColor);
            int maxEval = Integer.MIN_VALUE;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                int currentEval = (int) expectimax(duplicateBoard, depth - 1, currentColor.opposite(), false, false)[1];

                if (currentEval > maxEval) {
                    maxEval = currentEval;
                }
            }
            return new Object[]{null, maxEval};
        } else {
            List<Move> allPossibleMoves = getAllPossibleMoves(board, expectingColor);
            int expectedMinEval = 0;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                int currentEval = (int) expectimax(duplicateBoard, depth - 1, currentColor.opposite(), true, false)[1];
                expectedMinEval += currentEval;
            }

            int avgMinEval = expectedMinEval / allPossibleMoves.size();
            return new Object[]{null, avgMinEval};
        }
    }

    int calculatePieces(Board board, Color currentColor) {
        if (GameState.isMate(board, currentColor)) {
            if (currentColor == maximizingColor) {
                return -80000;
            } else if (currentColor == expectingColor) {
                return 80000;
            }
        } else if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(board) || GameState.isStaleMate(board, currentColor)) {
            return 0;
        }

        int finalScore = 0;
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
                    } else if (curPiece.getColor() == expectingColor){
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