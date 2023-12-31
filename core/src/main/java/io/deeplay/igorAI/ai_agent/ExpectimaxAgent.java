package io.deeplay.igorAI.ai_agent;

import io.deeplay.domain.Color;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import io.deeplay.service.BoardUtil;
import lombok.Setter;

import java.util.List;

public class ExpectimaxAgent extends AbstractAiAgent {
    /**
     * Setter для тестов функции оценки
     */
    @Setter
    private Color maximizingColor;
    @Setter
    private Color expectingColor;

    /**
     * Функция получения лучшего хода. Вызывает рекурсивную функцию expectimax,
     * которая просчитывает и возвращает лучший ход.
     * @param board текущее состояние доски
     * @param depth глубина просчетов
     * @param currentColor цвет текущего хода
     * @return лучший Move
     */
    public Move getBestMove(Board board, int depth, Color currentColor) {
        maximizingColor = currentColor;
        expectingColor = currentColor.opposite();
        return (Move) expectimax(board, depth, currentColor, true)[0];
    }

    /**
     * Рекурсивная функция экспектимакса. Проходит по каждому Move, делает ход,
     * изменяя состояние доски и вызывается до тех пор, пока либо глубина не станет равной 0,
     * либо не завершится игра. При одном из этих сценариев - вызывает функцию эвалюации и возвращает ее значение.
     * В конце возвращает лучший ход по проделанным просчетам.
     * @param board текущее состояние доски
     * @param depth глубина просчетов
     * @param currentColor цвет хода
     * @param maximizingPlayer является ли текущий ход максимизирующим
     * @return лучший ход, либо значение из функции эвалюации
     */
    public Object[] expectimax(Board board, int depth, Color currentColor, boolean maximizingPlayer) {
        if (depth == 0
                || GameState.isMate(board, currentColor)
                || GameState.isStaleMate(board, currentColor)
                || GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)) {
            return new Object[]{null, calculatePieces(board, currentColor)};
        }

        if (maximizingPlayer) {
            List<Move> allPossibleMoves = getAllPossibleMoves(board, maximizingColor);
            Move currentBestMove = getRandomMove(allPossibleMoves);
            int maxEval = Integer.MIN_VALUE + 1000;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                int currentEval = (int) expectimax(duplicateBoard, depth - 1, currentColor.opposite(), false)[1];

                if (currentEval > maxEval) {
                    maxEval = currentEval;
                    currentBestMove = move;
                }
            }
            return new Object[]{currentBestMove, maxEval};
        } else {
            List<Move> allPossibleMoves = getAllPossibleMoves(board, expectingColor);
            int expectedMinEval = 0;

            for (Move move : allPossibleMoves) {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                duplicateBoard.move(move);
                int currentEval = (int) expectimax(duplicateBoard, depth - 1, currentColor.opposite(), true)[1];
                expectedMinEval += currentEval;
            }

            int avgMinEval = expectedMinEval / allPossibleMoves.size();
            return new Object[]{null, avgMinEval};
        }
    }

    /** Функция эвалюации для Экспектимакса **/
    int calculatePieces(Board board, Color currentColor) {
        if (GameState.isMate(board, currentColor)) {
            if (currentColor == maximizingColor) return -8000000;
            else return 8000000;
        }

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