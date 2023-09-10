package io.deeplay.ai_agent;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MinimaxAgentTest {

    @Test
    void calculatePieces_defaultBoard() {
        final MinimaxAgent minimaxAgent = new MinimaxAgent();
        final Board board = new Board();
        final Color currentColor = Color.WHITE;

        Assertions.assertEquals(0, minimaxAgent.calculatePieces(board, currentColor));
    }

    @Test
    void calculatePieces_removedBlackPiece() {
        final MinimaxAgent minimaxAgent = new MinimaxAgent();
        final Board board = new Board();
        final Color currentColor = Color.WHITE;

        board.setPiece(new Coordinates(1, 7), new Empty(new Coordinates(1, 7)));

        Assertions.assertEquals(30, minimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(-30, minimaxAgent.calculatePieces(board, currentColor));
    }

    @Test
    void calculatePieces_childMate() {
        final MinimaxAgent minimaxAgent = new MinimaxAgent();
        final Board board = new Board();
        final Color currentColor = Color.WHITE;

        board.setPiece(new Coordinates(3, 7), new Empty(new Coordinates(3, 7)));
        board.setPiece(new Coordinates(4, 6), new Empty(new Coordinates(4, 6)));
        board.setPiece(new Coordinates(5, 1), new Empty(new Coordinates(5, 1)));
        board.setPiece(new Coordinates(6, 1), new Empty(new Coordinates(6, 1)));

        board.setPiece(new Coordinates(5, 2), new Pawn(new Coordinates(5, 2), Color.WHITE));
        board.setPiece(new Coordinates(6, 3), new Pawn(new Coordinates(6, 3), Color.WHITE));
        board.setPiece(new Coordinates(4, 4), new Pawn(new Coordinates(4, 4), Color.BLACK));
        board.setPiece(new Coordinates(7, 3), new Queen(new Coordinates(7, 3), Color.BLACK));

        Assertions.assertEquals(-8000000, minimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(8000000, minimaxAgent.calculatePieces(board, currentColor));
    }

    @Test
    void calculatePieces_stalemate() {
        final MinimaxAgent minimaxAgent = new MinimaxAgent();
        final Color currentColor = Color.WHITE;
        Board board = new Board();
        board.setBoard(Board.getEmptyBoard());

        board.setPiece(new Coordinates(2, 0), new King(new Coordinates(2, 0), Color.WHITE));
        board.setPiece(new Coordinates(2, 2), new King(new Coordinates(2, 2), Color.BLACK));
        board.setPiece(new Coordinates(3, 2), new Bishop(new Coordinates(3, 2), Color.BLACK));
        board.setPiece(new Coordinates(4, 2), new Knight(new Coordinates(4, 2), Color.BLACK));

        Assertions.assertEquals(0, minimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(-60, minimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(8000000, minimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(-8000000, minimaxAgent.calculatePieces(board, currentColor));
    }
}