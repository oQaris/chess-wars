package io.deeplay.igorAI.ai_agent;

import io.deeplay.domain.Color;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import io.deeplay.service.BoardUtil;
import lombok.Setter;

import java.util.*;

public class MinimaxAgent extends AbstractAiAgent {
    /**
     * Setter для тестов функции оценки
     */
    @Setter
    private Color maximizingColor;
    @Setter
    private Color minimizingColor;

    /**
     * Функция получения лучшего хода. Вызывает рекурсивную функцию minimax,
     * которая просчитывает и возвращает лучший ход.
     * @param board текущее состояние доски
     * @param depth глубина просчетов
     * @param alpha альфа
     * @param beta бета
     * @param currentColor цвет текущего хода
     * @return лучший Move
     */
    public Move getBestMove(Board board, int depth, int alpha, int beta, Color currentColor) {
        maximizingColor = currentColor;
        minimizingColor = currentColor.opposite();
        return (Move) minimax(board, depth, alpha, beta, currentColor, true)[0];
    }

    /**
     * Рекурсивная функция минимакса. Проходит по каждому Move, делает ход,
     * изменяя состояние доски и вызывается до тех пор, пока либо глубина не станет равной 0,
     * либо не завершится игра. При одном из этих сценариев - вызывает функцию эвалюации и возвращает ее значение.
     * В конце возвращает лучший ход по проделанным просчетам.
     * @param board текущее состояние доски
     * @param depth глубина просчетов
     * @param alpha альфа
     * @param beta бета
     * @param currentColor цвет хода
     * @param maximizingPlayer является ли текущий ход максимизирующим
     * @return лучший ход, либо значение из функции эвалюации
     */
    public Object[] minimax(Board board, int depth, int alpha, int beta, Color currentColor, boolean maximizingPlayer) {
        if (depth == 0
                || GameState.isStaleMate(board, currentColor)
                || GameState.isMate(board, currentColor)
                || GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)) {
            return new Object[]{null, calculatePieces(board, currentColor)};
        }

        if (maximizingPlayer) {
            List<Move> allPossibleMoves = getAllPossibleMoves(board, maximizingColor);
            Move currentBestMove = null;
            int maxEval = Integer.MIN_VALUE;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                int currentEval = (int) minimax(duplicateBoard, depth - 1, alpha, beta, currentColor.opposite(), false)[1];

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
            int minEval = Integer.MAX_VALUE;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                int currentEval = (int) minimax(duplicateBoard, depth - 1, alpha, beta, currentColor.opposite(), true)[1];

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

    /** Функция эвалюации для Минимакса **/
    int calculatePieces(Board board, Color currentColor) {
        if (GameState.isMate(board, currentColor))
            if (currentColor == maximizingColor) return -8000000;
            else return 8000000;

        if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(board) || GameState.isStaleMate(board, currentColor)) {
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
