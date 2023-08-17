package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStateTest {

    @Test
    void isCheck() {
        final Board board = new Board();
        //  board.setBoard(Board.getEmptyBoard());
        board.getPieceMoved()[0][0] = true;
        board.getPieceMoved()[4][0] = true;
        board.getPieceMoved()[7][0] = true;
        board.getPieceMoved()[0][7] = true;
        board.getPieceMoved()[4][7] = true;
        board.getPieceMoved()[7][7] = true;

        board.setPiece(new Coordinates(7, 0), new King(new Coordinates(7, 0), Color.WHITE));
        board.setPiece(new Coordinates(7, 7), new King(new Coordinates(7, 7), Color.BLACK));
        board.setPiece(new Coordinates(6, 7), new Queen(new Coordinates(6, 7), Color.BLACK));
        board.setPiece(new Coordinates(1, 1), new Rook(new Coordinates(1, 1), Color.BLACK));

        assertFalse(GameState.isCheck(board, Color.WHITE));

        board.move(new Move(new Coordinates(1, 1), new Coordinates(1, 0), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertTrue(GameState.isCheck(board, Color.WHITE));
    }

    @Test
    void isMate() {
        final Board board = new Board();
        board.setBoard(Board.getEmptyBoard());
        board.getPieceMoved()[0][0] = true;
        board.getPieceMoved()[4][0] = true;
        board.getPieceMoved()[7][0] = true;
        board.getPieceMoved()[0][7] = true;
        board.getPieceMoved()[4][7] = true;
        board.getPieceMoved()[7][7] = true;

        board.setPiece(new Coordinates(6, 0), new King(new Coordinates(6, 0), Color.WHITE));
        board.setPiece(new Coordinates(5, 1), new Pawn(new Coordinates(5, 1), Color.WHITE));
        board.setPiece(new Coordinates(6, 3), new Pawn(new Coordinates(6, 3), Color.WHITE));
        board.setPiece(new Coordinates(4, 2), new Pawn(new Coordinates(4, 2), Color.BLACK));
        board.setPiece(new Coordinates(3, 3), new Knight(new Coordinates(3, 3), Color.BLACK));
        board.setPiece(new Coordinates(1, 7), new Bishop(new Coordinates(1, 7), Color.BLACK));
        board.setPiece(new Coordinates(7, 7), new Rook(new Coordinates(7, 7), Color.BLACK));
        board.setPiece(new Coordinates(5, 7), new King(new Coordinates(5, 7), Color.BLACK));
        board.setPiece(new Coordinates(6, 7), new Queen(new Coordinates(6, 7), Color.BLACK));
        board.setPiece(new Coordinates(1, 2), new Rook(new Coordinates(1, 2), Color.BLACK));

        assertFalse(GameState.isMate(board, Color.WHITE));

        board.move(new Move(new Coordinates(1, 2), new Coordinates(1, 1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6, 0), new Coordinates(6, 1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6, 7), new Coordinates(6, 3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6, 1), new Coordinates(5, 0), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertFalse(GameState.isMate(board, Color.WHITE));

        board.move(new Move(new Coordinates(1, 1), new Coordinates(1, 0), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertTrue(GameState.isMate(board, Color.WHITE));
    }

    @Test
    void isStaleMate_simpleBoard() {
        final Board board = new Board();
        board.setBoard(Board.getEmptyBoard());
        board.getPieceMoved()[0][0] = true;
        board.getPieceMoved()[4][0] = true;
        board.getPieceMoved()[7][0] = true;
        board.getPieceMoved()[0][7] = true;
        board.getPieceMoved()[4][7] = true;
        board.getPieceMoved()[7][7] = true;

        board.setPiece(new Coordinates(6, 0), new King(new Coordinates(6, 0), Color.WHITE));
        board.setPiece(new Coordinates(6, 3), new King(new Coordinates(6, 3), Color.BLACK));
        board.setPiece(new Coordinates(4, 1), new Rook(new Coordinates(4, 1), Color.BLACK));

        board.move(new Move(new Coordinates(6, 3), new Coordinates(6, 2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6, 0), new Coordinates(7, 0), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertFalse(GameState.isStaleMate(board, Color.WHITE));

        board.move(new Move(new Coordinates(4, 1), new Coordinates(6, 1), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertTrue(GameState.isStaleMate(board, Color.WHITE));
    }

    @Test
    void isStaleMate_harderBoard() {
        final Board board = new Board();
        board.setBoard(Board.getEmptyBoard());
        board.getPieceMoved()[0][0] = true;
        board.getPieceMoved()[4][0] = true;
        board.getPieceMoved()[7][0] = true;
        board.getPieceMoved()[0][7] = true;
        board.getPieceMoved()[4][7] = true;
        board.getPieceMoved()[7][7] = true;

        board.setPiece(new Coordinates(0, 7), new King(new Coordinates(0, 7), Color.BLACK));
        board.setPiece(new Coordinates(6, 7), new Queen(new Coordinates(6, 7), Color.BLACK));
        board.setPiece(new Coordinates(7, 7), new Rook(new Coordinates(7, 7), Color.BLACK));
        board.setPiece(new Coordinates(2, 5), new Knight(new Coordinates(2, 5), Color.BLACK));
        board.setPiece(new Coordinates(0, 4), new Pawn(new Coordinates(0, 4), Color.BLACK));
        board.setPiece(new Coordinates(0, 2), new Rook(new Coordinates(0, 2), Color.BLACK));
        board.setPiece(new Coordinates(7, 5), new Bishop(new Coordinates(7, 5), Color.WHITE));
        board.setPiece(new Coordinates(2, 4), new Pawn(new Coordinates(2, 4), Color.WHITE));
        board.setPiece(new Coordinates(0, 3), new Pawn(new Coordinates(0, 3), Color.WHITE));
        board.setPiece(new Coordinates(7, 0), new King(new Coordinates(7, 0), Color.WHITE));

        board.move(new Move(new Coordinates(0, 2), new Coordinates(0, 1), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertTrue(GameState.isStaleMate(board, Color.WHITE));
        assertFalse(GameState.isStaleMate(board, Color.BLACK));
    }

    @Test
    void drawWithGameWithoutTakingAndAdvancingPawns() {
        final Board board = new Board();
        board.setBoard(Board.getEmptyBoard());
        board.getPieceMoved()[0][0] = true;
        board.getPieceMoved()[4][0] = true;
        board.getPieceMoved()[7][0] = true;
        board.getPieceMoved()[0][7] = true;
        board.getPieceMoved()[4][7] = true;
        board.getPieceMoved()[7][7] = true;

        board.setPiece(new Coordinates(0, 7), new King(new Coordinates(0, 7), Color.BLACK));
        board.setPiece(new Coordinates(1, 7), new Rook(new Coordinates(1, 7), Color.BLACK));
        board.setPiece(new Coordinates(7, 0), new King(new Coordinates(7, 0), Color.WHITE));
        board.setPiece(new Coordinates(7, 1), new Rook(new Coordinates(7, 1), Color.WHITE));

        for (int i = 0; i < 12; i++) {
            board.move(new Move(new Coordinates(1, 7), new Coordinates(1, 6), MoveType.ORDINARY, SwitchPieceType.NULL));
            board.move(new Move(new Coordinates(7, 1), new Coordinates(7, 2), MoveType.ORDINARY, SwitchPieceType.NULL));
            board.move(new Move(new Coordinates(1, 6), new Coordinates(1, 7), MoveType.ORDINARY, SwitchPieceType.NULL));
            board.move(new Move(new Coordinates(7, 2), new Coordinates(7, 1), MoveType.ORDINARY, SwitchPieceType.NULL));
        }

        assertFalse(GameState.drawWithGameWithoutTakingAndAdvancingPawns(board));

        board.move(new Move(new Coordinates(1, 7), new Coordinates(1, 6), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(7, 1), new Coordinates(7, 2), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertTrue(GameState.drawWithGameWithoutTakingAndAdvancingPawns(board));
    }
}