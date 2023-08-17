package io.deeplay.service;

import io.deeplay.exception.GameLogicException;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class BoardUtil {
    // private static final Logger logger = LogManager.getLogger(UserCommunicationService.class);

    public static Consumer<Board> duplicateBoard(Board board) {
        return duplicateBoard -> {
            if (board == null) throw new IllegalStateException();

            duplicateBoard.setBoard(new Piece[8][8]);

            if (board.getMoveHistory().getLastMove() != null) {
                Move lastMove = board.getMoveHistory().getLastMove();
                Move newMove = new Move(lastMove.startPosition(), lastMove.endPosition(), lastMove.moveType(),
                        lastMove.switchPieceType());
                duplicateBoard.getMoveHistory().addMove(newMove);
            }

            Piece piece;
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    piece = board.getPiece(new Coordinates(x,y));
                    duplicateBoard.setPiece(piece.getCoordinates(), copyPiece(piece));
                }
            }
        };
    }

    private static Piece copyPiece(Piece sourcePiece) {
        try {
            if (sourcePiece instanceof King) {
                return new King(new Coordinates(sourcePiece.getCoordinates().getX(), sourcePiece.getCoordinates().getY()),
                        sourcePiece.getColor());
            } else if (sourcePiece instanceof Queen) {
                return new Queen(new Coordinates(sourcePiece.getCoordinates().getX(), sourcePiece.getCoordinates().getY()),
                        sourcePiece.getColor());
            } else if (sourcePiece instanceof Bishop) {
                return new Bishop(new Coordinates(sourcePiece.getCoordinates().getX(), sourcePiece.getCoordinates().getY()),
                        sourcePiece.getColor());
            } else if (sourcePiece instanceof Knight) {
                return new Knight(new Coordinates(sourcePiece.getCoordinates().getX(), sourcePiece.getCoordinates().getY()),
                        sourcePiece.getColor());
            } else if (sourcePiece instanceof Pawn) {
                return new Pawn(new Coordinates(sourcePiece.getCoordinates().getX(), sourcePiece.getCoordinates().getY()),
                        sourcePiece.getColor());
            } else if (sourcePiece instanceof Rook) {
                return new Rook(new Coordinates(sourcePiece.getCoordinates().getX(), sourcePiece.getCoordinates().getY()),
                        sourcePiece.getColor());
            } else if (sourcePiece instanceof Empty) {
                return new Empty(new Coordinates(sourcePiece.getCoordinates().getX(), sourcePiece.getCoordinates().getY()));
            } else throw new GameLogicException();
        } catch (GameLogicException e) {

            log.error("Can't define " + sourcePiece + " type in copyPiece");
            throw new GameLogicException("Can't define sourcePiece type" + e);
        }
    }
}
