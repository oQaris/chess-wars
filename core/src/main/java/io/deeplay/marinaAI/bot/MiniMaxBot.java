package io.deeplay.marinaAI.bot;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.marinaAI.strategy.PestoStrategy;
import io.deeplay.marinaAI.strategy.Strategy;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Bot;
import io.deeplay.service.BoardUtil;
import io.deeplay.service.IUserCommunication;

import java.util.List;

public class MiniMaxBot extends Bot {
    private static final int MAX_DEPTH = 3;
    final Color maximizingColor;
    private Strategy strategy;

    public MiniMaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
        maximizingColor = color;
        strategy = new PestoStrategy(maximizingColor);
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    private int minMax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer, Color color) {
        if (depth == 0 || GameState.isGameOver(board, color)) {
            return strategy.evaluate(board);
        }

        if (maximizingPlayer) {
            return getMaxScore(board, depth, alpha, beta);
        } else {
            return getMinScore(board, depth, alpha, beta);
        }
    }

    @Override
    public Move getMove(Board board, Color color) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Piece> possiblePieces = getPiecesPossibleToMove(board, color);

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
                        Board promotionBoard = duplicateBoard(board);
                        switchPieceType = SwitchPieceType.values()[i];
                        Move currentMove = new Move(piece.getCoordinates(), move, moveType, switchPieceType);
                        promotionBoard.move(currentMove);

                        int score = minMax(promotionBoard, MAX_DEPTH - 1, bestScore, Integer.MAX_VALUE,
                                false, color);

                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = currentMove;
                        }
                    }
                }

                Move currentMove = new Move(piece.getCoordinates(), move, moveType, switchPieceType);
                duplicateBoard.move(currentMove);

                int score = minMax(duplicateBoard, MAX_DEPTH - 1, bestScore, Integer.MAX_VALUE,
                        false, color);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = currentMove;
                }
            }
        }

        return bestMove;
    }

    private Board duplicateBoard(Board board) {
        Board duplicateBoard = new Board();
        BoardUtil.duplicateBoard(board).accept(duplicateBoard);

        return duplicateBoard;
    }

    private int getMaxScore(Board board, int depth, int alpha, int beta) {
        int maxScore = Integer.MIN_VALUE;

        List<Piece> possiblePieces = getPiecesPossibleToMove(board, maximizingColor);

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

                        int score = minMax(promotionBoard, depth - 1, alpha, beta,
                                false, maximizingColor.opposite());
                        maxScore = Math.max(maxScore, score);

                        alpha = Math.max(alpha, score);

                        if (beta <= alpha) {
                            break;
                        }
                    }
                }

                Move currentMove = new Move(piece.getCoordinates(), move, moveType, switchPieceType);
                duplicateBoard.move(currentMove);

                int score = minMax(duplicateBoard, depth - 1, alpha, beta,
                        false, maximizingColor.opposite());
                maxScore = Math.max(maxScore, score);

                alpha = Math.max(alpha, score);

                if (beta <= alpha) {
                    break;
                }
            }
        }

        return maxScore;
    }

    private int getMinScore(Board board, int depth, int alpha, int beta) {
        int minScore = Integer.MAX_VALUE;
        List<Piece> possiblePieces = getPiecesPossibleToMove(board, maximizingColor.opposite());

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

                        int score = minMax(promotionBoard, depth - 1, alpha, beta,
                                true, maximizingColor);
                        minScore = Math.min(minScore, score);

                        beta = Math.min(beta, score);

                        if (beta <= alpha) {
                            break;
                        }
                    }
                }

                Move currentMove = new Move(piece.getCoordinates(), move, moveType, switchPieceType);
                duplicateBoard.move(currentMove);

                int score = minMax(duplicateBoard, depth - 1, alpha, beta,
                        true, maximizingColor);
                minScore = Math.min(minScore, score);

                beta = Math.min(beta, score);

                if (beta <= alpha) {
                    break;
                }
            }
        }

        return minScore;
    }
}