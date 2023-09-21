package io.deeplay.marinaAI.bot;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.marinaAI.utils.ScoredMove;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import io.deeplay.model.player.Bot;
import io.deeplay.service.BoardUtil;
import io.deeplay.service.IUserCommunication;

import java.util.List;

public class NegaMaxBot extends Bot {
    /**
     * Константа, определяющая максимальную глубину поиска в алгоритме ExpectiMax.
     */
    private static final int MAX_DEPTH = 3;

    /**
     * Цвет, за который играет бот и старается максимизировать.
     */
    private final Color maximizingColor;

    public NegaMaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
        maximizingColor = color;
    }

    @Override
    public Move getMove(Board board, Color currentColor) {
        ScoredMove bestMove = negaMax(board, MAX_DEPTH, currentColor, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return bestMove.getMove();
    }

    private ScoredMove negaMax(Board board, int depth, Color currentColor, int alpha, int beta) {
        if (depth == 0 || GameState.isGameOver(board, currentColor)) {
            int score = evaluate(board, currentColor);

            return new ScoredMove(score, null);
        }

        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Piece> possiblePieces = getPiecesPossibleToMove(board, currentColor);

        for (Piece piece : possiblePieces) {
            List<Coordinates> possibleMoves = piece.getPossibleMoves(board);
            List<Coordinates> movesWithoutCheck = GameState.getMovesWithoutMakingCheck(board, piece, possibleMoves);
            possibleMoves.retainAll(movesWithoutCheck);

            for (Coordinates move : possibleMoves) {
                Board duplicateBoard = duplicateBoard(board);
                MoveType moveType = getType(piece, move, duplicateBoard);
                SwitchPieceType switchPieceType = SwitchPieceType.NULL;

                if (moveType == MoveType.PROMOTION) {
                    for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                        Board promotionBoard = duplicateBoard(duplicateBoard);
                        switchPieceType = SwitchPieceType.values()[i];
                        Move currentMove = new Move(piece.getCoordinates(), move, moveType, switchPieceType);
                        promotionBoard.move(currentMove);

                        ScoredMove result = negaMax(promotionBoard, depth - 1,
                                currentColor.opposite(), -beta, -alpha);
                        int score = -result.getScore();

                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = currentMove;
                        }

                        alpha = Math.max(alpha, score);

                        if (alpha > beta) {
                            break;
                        }
                    }
                } else {
                    Move currentMove = new Move(piece.getCoordinates(), move, moveType, switchPieceType);
                    duplicateBoard.move(currentMove);

                    ScoredMove result = negaMax(duplicateBoard, depth - 1,
                            currentColor.opposite(), -beta, -alpha);
                    int score = -result.getScore();

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = currentMove;
                    }

                    alpha = Math.max(alpha, score);

                    if (alpha > beta) {
                        break;
                    }
                }
            }
        }

        return new ScoredMove(bestScore, bestMove);
    }

    /**
     * Метод создает копию доски.
     *
     * @param board исходная доска
     * @return копия доски
     */
    private Board duplicateBoard(Board board) {
        Board duplicateBoard = new Board();
        BoardUtil.duplicateBoard(board).accept(duplicateBoard);

        return duplicateBoard;
    }

    /**
     * Метод evaluate вычисляет оценку позиции на шахматной доске.
     *
     * @param board        шахматная доска, для которой нужно вычислить оценку
     * @param currentColor цвет текущего игрока
     * @return оценка позиции на доске
     */
    int evaluate(Board board, Color currentColor) {
        if (GameState.isMate(board, maximizingColor)) {
            return 100000000;
        }

        if (GameState.isStaleMate(board, maximizingColor)) {
            return 0;
        }

        if (GameState.isMate(board, maximizingColor.opposite())) {
            return -100000000;
        }

        if (GameState.isStaleMate(board, maximizingColor.opposite())) {
            return 0;
        }

        if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)) {
            return 0;
        }

        return getMaterialScore(board, currentColor);
    }

    /**
     * Метод getMaterialScore вычисляет оценку материального баланса на шахматной доске.
     *
     * @param board        шахматная доска, для которой нужно вычислить оценку
     * @param currentColor цвет текущего игрока
     * @return оценка материального баланса на доске
     */
    private int getMaterialScore(Board board, Color currentColor) {
        int finalScore = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(new Coordinates(i, j));

                if (piece.getColor() == currentColor) {
                    finalScore += getPieceScore(piece);
                } else if (piece.getColor() == currentColor.opposite()) {
                    finalScore -= getPieceScore(piece);
                }
            }
        }

        return finalScore;
    }

    /**
     * Метод getPieceScore вычисляет оценку для конкретной фигуры на шахматной доске.
     *
     * @param piece фигура, для которой нужно вычислить оценку
     * @return оценка для данной фигуры
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
