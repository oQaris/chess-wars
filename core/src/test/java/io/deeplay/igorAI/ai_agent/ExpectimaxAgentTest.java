package io.deeplay.igorAI.ai_agent;

import io.deeplay.igorAI.ai_agent.ExpectimaxAgent;
import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpectimaxAgentTest {
    @Test
    void calculatePieces_defaultBoard() {
        final ExpectimaxAgent expectimaxAgent = new ExpectimaxAgent();
        final Board board = new Board();
        final Color currentColor = Color.WHITE;

        Assertions.assertEquals(0, expectimaxAgent.calculatePieces(board, currentColor));
    }

    @Test
    void calculatePieces_removedBlackPiece() {
        final ExpectimaxAgent expectimaxAgent = new ExpectimaxAgent();
        final Board board = new Board();
        final Color currentColor = Color.WHITE;
        expectimaxAgent.setMaximizingColor(currentColor);
        expectimaxAgent.setExpectingColor(currentColor.opposite());

        board.setPiece(new Coordinates(1, 7), new Empty(new Coordinates(1, 7)));

        Assertions.assertEquals(30, expectimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(-30, expectimaxAgent.calculatePieces(board, currentColor));
    }

    @Test
    void calculatePieces_childMate() {
        final ExpectimaxAgent expectimaxAgent = new ExpectimaxAgent();
        final Board board = new Board();
        final Color currentColor = Color.WHITE;
        expectimaxAgent.setMaximizingColor(currentColor);
        expectimaxAgent.setExpectingColor(currentColor.opposite());

        board.setPiece(new Coordinates(3, 7), new Empty(new Coordinates(3, 7)));
        board.setPiece(new Coordinates(4, 6), new Empty(new Coordinates(4, 6)));
        board.setPiece(new Coordinates(5, 1), new Empty(new Coordinates(5, 1)));
        board.setPiece(new Coordinates(6, 1), new Empty(new Coordinates(6, 1)));

        board.setPiece(new Coordinates(5, 2), new Pawn(new Coordinates(5, 2), Color.WHITE));
        board.setPiece(new Coordinates(6, 3), new Pawn(new Coordinates(6, 3), Color.WHITE));
        board.setPiece(new Coordinates(4, 4), new Pawn(new Coordinates(4, 4), Color.BLACK));
        board.setPiece(new Coordinates(7, 3), new Queen(new Coordinates(7, 3), Color.BLACK));

        Assertions.assertEquals(-8000000, expectimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(8000000, expectimaxAgent.calculatePieces(board, currentColor));
    }

    @Test
    void calculatePieces_stalemate() {
        final ExpectimaxAgent expectimaxAgent = new ExpectimaxAgent();
        final Color currentColor = Color.WHITE;
        Board board = new Board();
        board.setBoard(Board.getEmptyBoard());

        board.setPiece(new Coordinates(2, 0), new King(new Coordinates(2, 0), Color.WHITE));
        board.setPiece(new Coordinates(2, 2), new King(new Coordinates(2, 2), Color.BLACK));
        board.setPiece(new Coordinates(3, 2), new Bishop(new Coordinates(3, 2), Color.BLACK));
        board.setPiece(new Coordinates(4, 2), new Knight(new Coordinates(4, 2), Color.BLACK));

        Assertions.assertEquals(0, expectimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(-60, expectimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(8000000, expectimaxAgent.calculatePieces(board, currentColor));
        Assertions.assertNotEquals(-8000000, expectimaxAgent.calculatePieces(board, currentColor));
    }

    @Test
    void testBestMoveOnMate() {
        Board board = new Board();

        board.move(new Move(new Coordinates(6,1), new Coordinates(6,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(4,6), new Coordinates(4,4), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(5,1), new Coordinates(5,2), MoveType.ORDINARY, SwitchPieceType.NULL));

        Move move = new ExpectimaxAgent().getBestMove(board, 3, Color.BLACK);

        Assertions.assertEquals(new Coordinates(3, 7), move.startPosition());
        Assertions.assertEquals(new Coordinates(7, 3), move.endPosition());

        board.move(move);

        Assertions.assertTrue(GameState.isMate(board, Color.WHITE));
    }

    @Test
    void testBestMoveOnTake() {
        Board board = new Board();

        board.move(new Move(new Coordinates(6,0), new Coordinates(5,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(4,6), new Coordinates(4,5), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(5,2), new Coordinates(6,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        Move move = new ExpectimaxAgent().getBestMove(board, 3, Color.BLACK);

        Assertions.assertEquals(new Coordinates(3, 7), move.startPosition());
        Assertions.assertEquals(new Coordinates(6, 4), move.endPosition());

        board.move(move);
    }
}