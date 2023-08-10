package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Empty;
import io.deeplay.model.piece.King;
import io.deeplay.model.piece.Pawn;
import io.deeplay.model.piece.Piece;

import java.util.ArrayList;
import java.util.List;

import static io.deeplay.model.Board.BOARD_HEIGHT;
import static io.deeplay.model.Board.BOARD_LENGTH;

public abstract class Player {
    protected Color color;

    public Player(Color color) {
        this.color = color;
    }

    public abstract Move getMove(Board board, Color currentColor);

    public List<Piece> getPiecesPossibleToMove(Board board, Color color) {
        List<Piece> movablePieces = new ArrayList<>();

        for (int x = 0; x < BOARD_HEIGHT; x++) {
            for (int y = 0; y < BOARD_LENGTH; y++) {
                Piece piece = board.getPiece(new Coordinates(x, y));
                if (!piece.getColor().equals(Color.EMPTY) && piece.getColor().equals(color)
                        && !piece.getPossibleMoves(board).isEmpty()) {
                    movablePieces.add(piece);
                }
            }
        }
        return movablePieces;
    }

    protected MoveType getType(Piece selectedPiece, Coordinates moveCoordinates, Board board) {
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

    public Color getColor() {
        return color;
    }
}
