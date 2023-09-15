package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Empty;
import io.deeplay.model.piece.King;
import io.deeplay.model.piece.Pawn;
import io.deeplay.model.piece.Piece;
import io.deeplay.service.IUserCommunication;

import java.util.ArrayList;
import java.util.List;

import static io.deeplay.model.Board.BOARD_HEIGHT;
import static io.deeplay.model.Board.BOARD_LENGTH;

public abstract class Player {
    protected Color color;

    public Player(Color color, IUserCommunication iUserCommunication) {
        this.color = color;
    }

    public abstract Move getMove(Board board, Color currentColor);

    /**
     * Метод возвращает все возможные фигуры, которыми можно походить
     *
     * @param board текущее состояние доски
     * @param color цвет текущего хода
     * @return лист из фигур
     */
    public static List<Piece> getPiecesPossibleToMove(Board board, Color color) {
        List<Piece> movablePieces = new ArrayList<>();

        for (int x = 0; x < BOARD_HEIGHT; x++) {
            for (int y = 0; y < BOARD_LENGTH; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));
                if (!piece.getColor().equals(Color.EMPTY) && piece.getColor().equals(color)
                        && !piece.getPossibleMoves(board).isEmpty()) {
                    if (!GameState.getMovesWithoutMakingCheck(board, piece, piece.getPossibleMoves(board)).isEmpty()) {
                        movablePieces.add(piece);
                    }
                }
            }
        }
        return movablePieces;
    }

    /**
     * Метод возвращает тип хода
     *
     * @param selectedPiece выбранная фигура для хода
     * @param moveCoordinates координаты выбранной фигуры
     * @param board текущее состояние доски
     * @return тип хода
     */
    public static MoveType getType(Piece selectedPiece, Coordinates moveCoordinates, Board board) {
        if (selectedPiece instanceof Pawn) {
            if (((Pawn) selectedPiece).isPromotion(moveCoordinates, board)) {
                return MoveType.PROMOTION;
            }
        }

        if (selectedPiece instanceof Pawn) {
            if (((Pawn) selectedPiece).isEnPassant(moveCoordinates, board)) {
                return MoveType.EN_PASSANT;
            }
        }

        if (!(board.getPiece(moveCoordinates) instanceof Empty)) return MoveType.TAKE;

        if (selectedPiece instanceof King) {
            if (Math.abs(selectedPiece.getCoordinates().getX() - moveCoordinates.getX()) == 2) return MoveType.CASTLING;
        }

        return MoveType.ORDINARY;
    }

    /**
     * Возвращает цвет игрока
     * @return цвет игрока
     */
    public Color getColor() {
        return color;
    }

    public Move getMove(Board board, Color currentColor, Coordinates pieceCoordinates, Coordinates moveCoordinates) {
        Piece selectedPiece = board.getPiece(pieceCoordinates);
        MoveType moveType = getType(selectedPiece, moveCoordinates, board);
        SwitchPieceType selectedSwitchPiece = SwitchPieceType.NULL;

        System.out.println("You selected " + selectedPiece.getColor().name() + " "
                + selectedPiece.getClass().getSimpleName() + " to move to coordinates x:" + moveCoordinates.getX()
                + " y:" + moveCoordinates.getY());

        return new Move(selectedPiece.getCoordinates(), moveCoordinates, moveType, selectedSwitchPiece);
    }
}
