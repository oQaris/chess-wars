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
import io.deeplay.model.player.Player;
import io.deeplay.service.BoardUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GameState {
    static List<Object> errorList;
    public static List<Object> getErrorList() {
        return errorList;
    }

    /**
     * Метод проверяет шах при текущем состоянии доски
     * @param board текущее состояние доски
     * @param color цвет игрока, чьи фигуры проверяют на шах
     * @return есть ли шах
     */
    public static boolean isCheck(Board board, Color color) {
        Coordinates kingPosition = getKingPosition(board, color);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));
                if (!(piece instanceof Empty) && piece.getColor() != color) {
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
     * @param board текущее состояние доски
     * @param color цвет игрока, которого проверяют
     * @return есть ли мат
     */
    public static boolean isMate(Board board, Color color) {
        if (!isCheck(board, color)) {
            return false;
        }

        return isStaleMate(board, color);
    }

    /**
     * Метод проверяет пат при текущем состоянии доски
     * @param board текущее состояние доски
     * @param color цвет игрока, которого проверяют
     * @return есть ли пат
     */
    public static boolean isStaleMate(Board board, Color color) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));

                if (!(piece instanceof Empty) && piece.getColor() == color) {
                    for (Coordinates coordinates : piece.getPossibleMoves(board)) {
                        MoveType moveType = Player.getType(piece, coordinates, board);
                        if (moveType == MoveType.PROMOTION) {
                            for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                                Board duplicateBoard = new Board();
                                BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                                SwitchPieceType switchPieceType = SwitchPieceType.values()[i];
                                duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                                        moveType, switchPieceType));
                                if (!isCheck(duplicateBoard, color)) {
                                    return false;
                                }
                            }
                        } else {
                            Board duplicateBoard = new Board();
                            BoardUtil.duplicateBoard(board).accept(duplicateBoard);
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
     * @param board текущее состояние доски
     * @return было ли 50 ходов без изменения положения на доске
     */
    public static boolean drawWithGameWithoutTakingAndAdvancingPawns(Board board) {
        return board.getMoveHistory().getMovesWithoutTake() >= 50;
    }

    /**
     * Метод проверяет каждый возможный ход переданной фигуры, и если он не подставляет короля, то добавляет эти
     * координаты в лист. Затем его возвращает
     * @param board текущее состояние доски
     * @param piece текущая фигура
     * @param potentialCoordinates координаты, куда может походить фигура
     * @return лист из координат, куда фигура может походить без подставления своего короля
     */
    public static List<Coordinates> getMovesWithoutMakingCheck(Board board, Piece piece,
                                                               List<Coordinates> potentialCoordinates) {
        List<Coordinates> rightMoves = new ArrayList<>();
        Color color = piece.getColor();

        for (Coordinates coordinates : potentialCoordinates) {
            MoveType moveType = Player.getType(piece, coordinates, board);
            if (moveType == MoveType.PROMOTION) {
                for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                    Board duplicateBoard = new Board();
                    BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                    SwitchPieceType switchPieceType = SwitchPieceType.values()[i];
                    duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                            moveType, switchPieceType));
                    if (!isCheck(duplicateBoard, color)) {
                        rightMoves.add(coordinates);
                    }
                }
            } else {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                SwitchPieceType switchPieceType = SwitchPieceType.NULL;
                duplicateBoard.move(new Move(piece.getCoordinates(), coordinates,
                        moveType, switchPieceType));

                if (!isCheck(duplicateBoard, color)) {
                    rightMoves.add(coordinates);
                }
            }
        }

        return rightMoves;
    }

    /**
     * Метод проверяет каждый возможный ход переданной фигуры, и если он не подставляет короля,
     * то создает Move и добавляет его в лист. Затем этот лист возвращает
     * @param board текущее состояние доски
     * @param piece текущая фигура
     * @param potentialCoordinates координаты, куда может походить фигура
     * @return лист из Move, куда фигура может походить без подставления своего короля
     */
    public static List<Move> getMovesListWithoutMakingCheck(Board board, Piece piece,
                                                            List<Coordinates> potentialCoordinates) {
        List<Move> rightMoves = new ArrayList<>();

        for (Coordinates coordinates : potentialCoordinates) {
            MoveType moveType = Player.getType(piece, coordinates, board);
            if (moveType == MoveType.PROMOTION) {
                for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                    Board duplicateBoard = new Board();
                    BoardUtil.duplicateBoard(board).accept(duplicateBoard);

                    SwitchPieceType switchPieceType = SwitchPieceType.values()[i];
                    Move potentialMove = new Move(piece.getCoordinates(), coordinates,
                            moveType, switchPieceType);
                    duplicateBoard.move(potentialMove);
                    if (!isCheck(duplicateBoard, piece.getColor())) {
                        rightMoves.add(potentialMove);
                    }
                }
            } else {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                SwitchPieceType switchPieceType = SwitchPieceType.NULL;
                Move potentialMove = new Move(piece.getCoordinates(), coordinates,
                        moveType, switchPieceType);
                duplicateBoard.move(potentialMove);
                if (!isCheck(duplicateBoard, piece.getColor())) {
                    rightMoves.add(potentialMove);
                }
            }
        }

        return rightMoves;
    }

    /**
     * Метод ищет короля заданного цвета и возвращает его координаты
     * @param board     текущее состояние доски
     * @param kingColor цвет короля
     * @return координаты короля или выбрасывает ошибку
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

        String errorMessage = " king exception";
        errorList.add(new GameLogicException(errorMessage));
        errorList.add(errorMessage);

        throw new GameLogicException(kingColor + " king is not on the board");
    }

    /**
     * Проверка на конец игры. По-очереди вызывает проверку на пат, на мат и на ничью
     * @param board текущее состояние доски
     * @param color текущий цвет
     * @return конец игры или нет
     */
    public static boolean isGameOver(Board board, Color color) {
        return isStaleMate(board, color) || isMate(board, color) || drawWithGameWithoutTakingAndAdvancingPawns(board);
    }
}
