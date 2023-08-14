package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Empty;
import io.deeplay.model.piece.King;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Human;
import io.deeplay.service.BoardUtil;

import java.util.List;

public class GameState {
    public static boolean isCheck(Board board, Color color) {
        Coordinates kingPosition = getKingPosition(board, color);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));
                if(!(piece instanceof Empty) && piece.getColor() != color) {
                    if (piece.canMoveAt(kingPosition, board)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isMate(Board board, Color currentColor) {
        Color enemyPlayerColor = currentColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        if (!isCheck(board, enemyPlayerColor)) {
            return false;
        }

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));

                if (!(piece instanceof Empty) && piece.getColor() == enemyPlayerColor) {
                    for (Coordinates coordinates : piece.getPossibleMoves(board)) {
                        Board duplicateBoard = new Board();
                        BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                        Human tempHuman = new Human(enemyPlayerColor);

                        MoveType moveType = tempHuman.getType(piece, coordinates, duplicateBoard);
                        if (moveType == MoveType.PROMOTION) {
                            for (int i = 0; i < SwitchPieceType.values().length; i++) {
                                SwitchPieceType switchPieceType = SwitchPieceType.values()[i];
                                duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                                        moveType, switchPieceType));
                                if (!isCheck(duplicateBoard, enemyPlayerColor)) {
                                    return false;
                                }
                            }
                        } else {
                            SwitchPieceType switchPieceType = SwitchPieceType.NULL;
                            duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                                    moveType, switchPieceType));
                            if (!isCheck(duplicateBoard, enemyPlayerColor)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public static boolean isStaleMate(Board board, Color color) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));

                if (!(piece instanceof Empty) && piece.getColor() == color) {
                    List<Coordinates> allMoves = piece.getPossibleMoves(board);
                    if (!allMoves.isEmpty()) {
                        for (Coordinates coordinates : allMoves) {
                            Board duplicateBoard = new Board();
                            BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                            Human tempHuman = new Human(color);

                            MoveType moveType = tempHuman.getType(piece, coordinates, duplicateBoard);
                            if (moveType == MoveType.PROMOTION) {
                                for (int i = 0; i < SwitchPieceType.values().length; i++) {
                                    SwitchPieceType switchPieceType = SwitchPieceType.values()[i];
                                    duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                                            moveType, switchPieceType));
                                    if (!isCheck(duplicateBoard, color)) {
                                        return false;
                                    }
                                }
                            } else {
                                SwitchPieceType switchPieceType = SwitchPieceType.NULL;
                                duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                                        moveType, switchPieceType));
                                if (!isCheck(duplicateBoard, color)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public static boolean drawWithGameWithoutTakingAndAdvancingPawns(Board board) {
        return board.getMoveHistory().getMovesWithoutTake() >= 50;
    }

    private static Coordinates getKingPosition(Board board, Color kingColor) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));
                if (piece.getColor() == kingColor && piece instanceof King) return new Coordinates(x, y);
            }
        }
        return null; //пробросить потом ошибку
    }
}
