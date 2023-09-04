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

public class MinimaxAgent {
    private Color maximizingColor;
    private Color minimizingColor;

    public Move getBestMove(Board board, int depth, double alpha, double beta, Color currentColor) {
        maximizingColor = currentColor;
        minimizingColor = currentColor.opposite();
        boolean maximizingPlayer = true;
        return (Move) minimax(board, depth, alpha, beta, currentColor, maximizingPlayer)[0];
    }

    public Object[] minimax(Board board, int depth, double alpha, double beta, Color currentColor, boolean maximizingPlayer) {
        if (depth == 0
                || GameState.isMate(board, currentColor)
                || GameState.isStaleMate(board, currentColor)
                || GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)) {
            return new Object[]{null, calculatePieces(board, currentColor)};
        }

        if (maximizingPlayer) {
            List<Piece> possiblePiecesToMove = getPiecesPossibleToMove(board, maximizingColor);
            Collections.shuffle(possiblePiecesToMove);
            Move currentBestMove = randomMove(board, possiblePiecesToMove);
            double maxEval = Double.MIN_VALUE;

            for (Piece piece : possiblePiecesToMove) {
                List<Coordinates> moveCoordinates = piece.getPossibleMoves(board);
                List<Coordinates> movesWithoutCheck = GameState.getMovesWithoutMakingCheck(board, piece, moveCoordinates);
                moveCoordinates.retainAll(movesWithoutCheck);

                for (Coordinates coordinates : moveCoordinates) {
                    Board duplicateBoard = new Board();
                    BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                    MoveType moveType = Player.getType(piece, coordinates, duplicateBoard);
                    double currentEval;
                    SwitchPieceType switchPieceType;
                    if (moveType == MoveType.PROMOTION) {
                        for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                            BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                            switchPieceType = SwitchPieceType.values()[i];
                            Move move = new Move(piece.getCoordinates(), coordinates, moveType, switchPieceType);
                            duplicateBoard.move(move);
                            currentEval = (double) minimax(duplicateBoard, depth - 1, alpha, beta, currentColor.opposite(), false)[1];

                            if (currentEval > maxEval) {
                                maxEval = currentEval;
                                currentBestMove = move;
                            }

                            alpha = Math.max(alpha, currentEval);
                            if (beta <= alpha) {
                                break; // Beta cut-off
                            }
                        }
                    } else {
                        switchPieceType = SwitchPieceType.NULL;
                        Move move = new Move(piece.getCoordinates(), coordinates, moveType, switchPieceType);
                        duplicateBoard.move(move);
                        currentEval = (double) minimax(duplicateBoard, depth - 1, alpha, beta, currentColor.opposite(), false)[1];

                        if (currentEval > maxEval) {
                            maxEval = currentEval;
                            currentBestMove = move;
                        }

                        alpha = Math.max(alpha, currentEval);
                        if (beta <= alpha) {
                            break; // Beta cut-off
                        }
                    }
                }
            }
            return new Object[]{currentBestMove, maxEval};
        } else {
            List<Piece> possiblePiecesToMove = getPiecesPossibleToMove(board, minimizingColor);
            Collections.shuffle(possiblePiecesToMove);
            Move currentBestMove = randomMove(board, possiblePiecesToMove);
            double minEval = Double.MAX_VALUE;

            for (Piece piece : possiblePiecesToMove) {
                List<Coordinates> moveCoordinates = piece.getPossibleMoves(board);
                List<Coordinates> movesWithoutCheck = GameState.getMovesWithoutMakingCheck(board, piece, moveCoordinates);
                moveCoordinates.retainAll(movesWithoutCheck);

                for (Coordinates coordinates : moveCoordinates) {
                    Board duplicateBoard = new Board();
                    BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                    MoveType moveType = Player.getType(piece, coordinates, duplicateBoard);
                    double currentEval;
                    SwitchPieceType switchPieceType;
                    if (moveType == MoveType.PROMOTION) {
                        for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                            BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                            switchPieceType = SwitchPieceType.values()[i];
                            Move move = new Move(piece.getCoordinates(), coordinates, moveType, switchPieceType);
                            duplicateBoard.move(move);
                            currentEval = (double) minimax(duplicateBoard, depth - 1, alpha, beta, currentColor.opposite(), true)[1];

                            if (currentEval < minEval) {
                                minEval = currentEval;
                                currentBestMove = move;
                            }

                            beta = Math.min(beta, currentEval);
                            if (beta <= alpha) {
                                break; // Alpha cut-off
                            }
                        }
                    } else {
                        switchPieceType = SwitchPieceType.NULL;
                        Move move = new Move(piece.getCoordinates(), coordinates, moveType, switchPieceType);
                        duplicateBoard.move(move);
                        currentEval = (double) minimax(duplicateBoard, depth - 1, alpha, beta, currentColor.opposite(), true)[1];

                        if (currentEval < minEval) {
                            minEval = currentEval;
                            currentBestMove = move;
                        }

                        beta = Math.min(beta, currentEval);
                        if (beta <= alpha) {
                            break; // Alpha cut-off
                        }
                    }
                }
            }
            return new Object[]{currentBestMove, minEval};
        }
    }

    Move randomMove(Board board, List<Piece> randomPiecesList) {
        Random random = new Random();
        Piece randomPiece;
        if (randomPiecesList.size() == 1) randomPiece = randomPiecesList.get(0);
        else randomPiece = randomPiecesList.get(random.nextInt(randomPiecesList.size() - 1));

        List<Coordinates> availableMoves = randomPiece.getPossibleMoves(board);
        List<Coordinates> movesWithoutCheck = GameState.getMovesWithoutMakingCheck(board, randomPiece, availableMoves);
        availableMoves.retainAll(movesWithoutCheck);

        Coordinates randomMoveCoordinates = null;
        if (availableMoves.size() == 1) randomMoveCoordinates = availableMoves.get(0);
        else randomMoveCoordinates = availableMoves.get(random.nextInt(availableMoves.size() - 1));

        MoveType moveType = getType(randomPiece, randomMoveCoordinates, board);
        SwitchPieceType selectedSwitchPiece = SwitchPieceType.NULL;

        if (moveType == MoveType.PROMOTION) {
            switch (SwitchPieceType.getRandomPiece()) {
                case BISHOP -> selectedSwitchPiece = SwitchPieceType.BISHOP;
                case KNIGHT -> selectedSwitchPiece = SwitchPieceType.KNIGHT;
                case QUEEN -> selectedSwitchPiece = SwitchPieceType.QUEEN;
                case ROOK -> selectedSwitchPiece = SwitchPieceType.ROOK;
            }
        }
        return new Move(randomPiece.getCoordinates(), randomMoveCoordinates, moveType, selectedSwitchPiece);
    }

    double calculatePieces(Board board, Color currentColor) {
        if (GameState.isMate(board, currentColor)) {
            if (currentColor.opposite() == maximizingColor) {
                return 8000000;
            } else if (currentColor.opposite() == minimizingColor) {
                return -8000000;
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
