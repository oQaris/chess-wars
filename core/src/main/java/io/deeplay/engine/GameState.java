package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.exception.GameLogicException;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Empty;
import io.deeplay.model.piece.King;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;
import io.deeplay.service.BoardUtil;
import io.deeplay.service.UserCommunicationService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GameState {
    /**
     * Метод проверяет шах при текущем состоянии доски
     *
     * @param board текущее состояние доски
     * @param color цвет игрока, чьи фигуры проверяют на шах
     * @return есть ли шах
     */
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

    /**
     * Метод проверяет мат при текущем состоянии доски
     *
     * @param board текущее состояние доски
     * @param color цвет игрока, которого проверяют
     * @return есть ли мат
     */
    public static boolean isMate(Board board, Color color) {
        if (!isCheck(board, color)) {
            return false;
        }

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));

                if (!(piece instanceof Empty) && piece.getColor() == color) {
                    for (Coordinates coordinates : piece.getPossibleMoves(board)) {
                        Board duplicateBoard = new Board();
                        BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                        Human tempHuman = new Human(color, new UserCommunicationService(System.in, System.out));

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

        return true;
    }

    /**
     * Метод проверяет пат при текущем состоянии доски
     *
     * @param board текущее состояние доски
     * @param color цвет игрока, которого проверяют
     * @return есть ли мат
     */
    public static boolean isStaleMate(Board board, Color color) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));

                if (!(piece instanceof Empty) && piece.getColor() == color) {
                    for (Coordinates coordinates : piece.getPossibleMoves(board)) {
                        Board duplicateBoard = new Board();
                        BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                        MoveType moveType = Player.getType(piece, coordinates, duplicateBoard);
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

        return true;
    }

    /**
     * Метод проверяет, если ходов без изменения состояния больше 50, то это ничья
     *
     * @param board текущее состояние доски
     * @return было ли 50 ходов без изменения положения на доске
     */
    public static boolean drawWithGameWithoutTakingAndAdvancingPawns(Board board) {
        return board.getMoveHistory().getMovesWithoutTake() >= 50;
    }

    public static List<Coordinates> getMovesWithoutMakingCheck(Board board, Piece piece,
                                                               List<Coordinates> potentialCoordinates) {
        List<Coordinates> rightMoves = new ArrayList<>();

        for (Coordinates coordinates : potentialCoordinates) {
            Board duplicateBoard = new Board();
            BoardUtil.duplicateBoard(board).accept(duplicateBoard);

            MoveType moveType = Player.getType(piece, coordinates, duplicateBoard);
            if (moveType == MoveType.PROMOTION) {
                for (int i = 0; i < SwitchPieceType.values().length; i++) {
                    SwitchPieceType switchPieceType = SwitchPieceType.values()[i];
                    duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                            moveType, switchPieceType));
                    if (!isCheck(duplicateBoard, piece.getColor())) {
                        rightMoves.add(coordinates);
                    }
                }
            } else {
                SwitchPieceType switchPieceType = SwitchPieceType.NULL;
                duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                        moveType, switchPieceType));
                if (!isCheck(duplicateBoard, piece.getColor())) {
                    rightMoves.add(coordinates);
                }
            }
        }

        return rightMoves;
    }

    /**
     * Метод ищет короля заданного цвета и возвращает его координаты
     *
     * @param board текущее состояние доски
     * @param kingColor цвет короля
     * @return координаты короля
     */
    private static Coordinates getKingPosition(Board board, Color kingColor) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));
                if (!(piece instanceof Empty)) {
                    if (piece.getColor() == kingColor && piece instanceof King) {
                        log.info(kingColor + " king is on coordinates x=" + x + ", y=" + y);
                        return new Coordinates(x, y);
                    }
                }
            }
        }
        log.error(kingColor + " king is not on the board. Throw GameLogicException...");
        throw new GameLogicException(kingColor + " king is not on the board"); //пробросить потом ошибку
    }
}
