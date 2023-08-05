package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.engine.GameInfo;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.deeplay.model.Board.printBoard;
import static io.deeplay.model.Board.printBoardOnce;
import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private Board board;
    private Piece pawn;
    private Piece whitePawn;
    private Piece blackPawn;
    private final int X_INITIAL_COORDINATE = 1;
    private final int Y_INITIAL_COORDINATE_WHITE = 1;
    private final int Y_INITIAL_COORDINATE_BLACK = 6;

    @BeforeEach
    void setUp() {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        board = new Board();
        pawn = board.getPiece(new Coordinates(0, 1));

        whitePawn = board.getPiece(new Coordinates(X_INITIAL_COORDINATE, Y_INITIAL_COORDINATE_WHITE));
        blackPawn = board.getPiece(new Coordinates(X_INITIAL_COORDINATE, Y_INITIAL_COORDINATE_BLACK));
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, pawn.getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(0, 6)).getColor());
    }

    @Test
    void testMoveOnOneCellForward() {
        Coordinates whiteMoveCoordinates = new Coordinates(1, 2);
        Coordinates blackMoveCoordinates = new Coordinates(1, 5);

        assertTrue(whitePawn.canMoveAt(whiteMoveCoordinates, board));
        assertTrue(blackPawn.canMoveAt(blackMoveCoordinates, board));
    }

    @Test
    void testMoveOnTwoCellForward() {
        Coordinates whiteMoveCoordinates = new Coordinates(1, 3);
        Coordinates blackMoveCoordinates = new Coordinates(1, 4);

        assertTrue(whitePawn.canMoveAt(whiteMoveCoordinates, board));
        assertTrue(blackPawn.canMoveAt(blackMoveCoordinates, board));
    }

    @Test
    void testWhiteCanTakeLeftDiagonalPiece() {
        Coordinates whiteMoveCoordinates = new Coordinates(0, 2);
        board.setPiece(new Coordinates(0, 2), new Bishop(new Coordinates(0, 2), Color.BLACK));
        assertTrue(whitePawn.canMoveAt(whiteMoveCoordinates, board));
    }

    @Test
    void testWhiteCantTakeLeftDiagonalPieceWithSameColor() {
        Coordinates whiteMoveCoordinates = new Coordinates(0, 2);
        board.setPiece(new Coordinates(0, 2), new Bishop(new Coordinates(0, 2), Color.WHITE));
        assertFalse(whitePawn.canMoveAt(whiteMoveCoordinates, board));
    }

    @Test
    void testBlackCanTakeLeftDiagonalPiece() {
        Coordinates blackMoveCoordinates = new Coordinates(0, 5);
        board.setPiece(new Coordinates(0, 5), new Bishop(new Coordinates(0, 5), Color.WHITE));
        assertTrue(blackPawn.canMoveAt(blackMoveCoordinates, board));
    }

    @Test
    void testBlackCantTakeLeftDiagonalPieceWithSameColor() {
        Coordinates blackMoveCoordinates = new Coordinates(0, 5);
        board.setPiece(new Coordinates(0, 5), new Bishop(new Coordinates(0, 5), Color.BLACK));
        assertFalse(blackPawn.canMoveAt(blackMoveCoordinates, board));
    }

    @Test
    void testWhiteCanTakeRightDiagonalPiece() {
        Coordinates whiteMoveCoordinates = new Coordinates(2, 2);
        board.setPiece(new Coordinates(2, 2), new Bishop(new Coordinates(2, 2), Color.BLACK));
        assertTrue(whitePawn.canMoveAt(whiteMoveCoordinates, board));
    }

    @Test
    void testWhiteCantTakeRightDiagonalPieceWithSameColor() {
        Coordinates whiteMoveCoordinates = new Coordinates(2, 2);
        board.setPiece(new Coordinates(2, 2), new Bishop(new Coordinates(2, 2), Color.WHITE));
        assertFalse(whitePawn.canMoveAt(whiteMoveCoordinates, board));
    }

    @Test
    void testBlackCanTakeRightDiagonalPiece() {
        Coordinates blackMoveCoordinates = new Coordinates(2, 5);
        board.setPiece(new Coordinates(2, 5), new Bishop(new Coordinates(2, 5), Color.WHITE));
        assertTrue(blackPawn.canMoveAt(blackMoveCoordinates, board));
    }

    @Test
    void testBlackCantTakeRightDiagonalPieceWithSameColor() {
        Coordinates blackMoveCoordinates = new Coordinates(2, 5);
        board.setPiece(new Coordinates(2, 5), new Bishop(new Coordinates(2, 5), Color.BLACK));
        assertFalse(blackPawn.canMoveAt(blackMoveCoordinates, board));
    }

    @Test
    void testWhitePawnCantTakeForward() {
        board.setPiece(new Coordinates(1, 2), new Pawn(new Coordinates(1, 2), Color.BLACK));
        Coordinates whiteMoveCoordinates = new Coordinates(1, 2);
        assertFalse(whitePawn.canMoveAt(whiteMoveCoordinates, board));
    }

    @Test
    void testBlackPawnCantTakeForward() {
        board.setPiece(new Coordinates(1, 5), new Pawn(new Coordinates(1, 5), Color.WHITE));
        Coordinates blackMoveCoordinates = new Coordinates(1, 5);
        assertFalse(blackPawn.canMoveAt(blackMoveCoordinates, board));
    }

    @Test
    void testWhitePawnCantGoBack() {
        Coordinates wrongMoveCoordinates1 = new Coordinates(1,3);
        Coordinates wrongMoveCoordinates2 = new Coordinates(1,2);

        board.setPiece(new Coordinates(1, 4), new Pawn(new Coordinates(1, 4), Color.WHITE));
        final Piece testPawn = board.getPiece(new Coordinates(1, 4));

        assertFalse(testPawn.canMoveAt(wrongMoveCoordinates1, board));
        assertFalse(testPawn.canMoveAt(wrongMoveCoordinates2, board));
    }

    @Test
    void testBlackPawnCantGoBack() {
        Coordinates wrongMoveCoordinates1 = new Coordinates(1,4);
        Coordinates wrongMoveCoordinates2 = new Coordinates(1,5);

        board.setPiece(new Coordinates(1, 3), new Pawn(new Coordinates(1, 3), Color.BLACK));
        final Piece testPawn = board.getPiece(new Coordinates(1, 3));

        assertFalse(testPawn.canMoveAt(wrongMoveCoordinates1, board));
        assertFalse(testPawn.canMoveAt(wrongMoveCoordinates2, board));
    }

    @Test
    void testWhiteCantMoveOnTwoCellForwardWhenOtherFigureBlocks() {
        Coordinates whiteMoveCoordinates = new Coordinates(1, 3);

        board.setPiece(new Coordinates(1, 2), new Bishop(new Coordinates(1, 2), Color.BLACK));
        assertFalse(whitePawn.canMoveAt(whiteMoveCoordinates, board));

        board.setPiece(new Coordinates(1, 2), new Bishop(new Coordinates(1, 2), Color.WHITE));
        assertFalse(whitePawn.canMoveAt(whiteMoveCoordinates, board));
    }

    @Test
    void testBlackCantMoveOnTwoCellForwardWhenOtherFigureBlocks() {
        Coordinates blackMoveCoordinates = new Coordinates(1, 4);

        board.setPiece(new Coordinates(1, 5), new Bishop(new Coordinates(1, 5), Color.BLACK));
        assertFalse(blackPawn.canMoveAt(blackMoveCoordinates, board));

        board.setPiece(new Coordinates(1, 5), new Bishop(new Coordinates(1, 5), Color.WHITE));
        assertFalse(blackPawn.canMoveAt(blackMoveCoordinates, board));
    }

    @Test
    void testWhiteCantGoBackDiagonally() {
        Coordinates wrongMoveCoordinates1 = new Coordinates(0,3);
        Coordinates wrongMoveCoordinates2 = new Coordinates(2,3);

        board.setPiece(new Coordinates(1, 4), new Pawn(new Coordinates(1, 4), Color.WHITE));
        final Piece testPawn = board.getPiece(new Coordinates(1, 4));

        assertFalse(testPawn.canMoveAt(wrongMoveCoordinates1, board));
        assertFalse(testPawn.canMoveAt(wrongMoveCoordinates2, board));
    }

    @Test
    void testBlackCantGoBackDiagonally() {
        Coordinates wrongMoveCoordinates1 = new Coordinates(0,4);
        Coordinates wrongMoveCoordinates2 = new Coordinates(2,4);

        board.setPiece(new Coordinates(1, 3), new Pawn(new Coordinates(1, 3), Color.BLACK));
        final Piece testPawn = board.getPiece(new Coordinates(1, 3));

        assertFalse(testPawn.canMoveAt(wrongMoveCoordinates1, board));
        assertFalse(testPawn.canMoveAt(wrongMoveCoordinates2, board));
    }

    @Test
    void testCantGoOutside() {
        final Piece testLeftWhitePawn = board.getPiece(new Coordinates(0, 1));
        final Piece testRightWhitePawn = board.getPiece(new Coordinates(7, 1));

        final Piece testLeftBlackPawn = board.getPiece(new Coordinates(0, 6));
        final Piece testRightBlackPawn = board.getPiece(new Coordinates(7, 6));

        final Coordinates leftWhiteCoordinates = new Coordinates(-1, 2);
        final Coordinates rightWhiteCoordinates = new Coordinates(8, 2);
        final Coordinates leftBlackCoordinates = new Coordinates(-1, 5);
        final Coordinates rightBlackCoordinates = new Coordinates(8, 5);

        assertFalse(testLeftWhitePawn.canMoveAt(leftWhiteCoordinates, board));
        assertFalse(testLeftBlackPawn.canMoveAt(leftBlackCoordinates, board));
        assertFalse(testRightWhitePawn.canMoveAt(rightWhiteCoordinates, board));
        assertFalse(testRightBlackPawn.canMoveAt(rightBlackCoordinates, board));
    }

    // Пока не работает из-за отсутствия функционала для этого теста

//    @Test
//    void testTakeOnThePass() {
//        Coordinates whitePieceCoordinates = new Coordinates(2,4);
//        Coordinates blackPieceCoordinates = new Coordinates(1,6);
//        Coordinates blackPieceMoveCoordinates = new Coordinates(1,4);
//
//        board.setPiece(whitePieceCoordinates, new Pawn(whitePieceCoordinates, Color.WHITE));
//        board.move(new Move(blackPieceCoordinates, blackPieceMoveCoordinates, MoveType.ORDINARY, '\0'));
//
//        Piece whitePiece = board.getPiece(whitePieceCoordinates);
//        assertTrue(whitePiece.canMoveAt(new Coordinates(1, 5), board));
//
//        board.move(new Move(new Coordinates(5,1), new Coordinates(5, 3), MoveType.ORDINARY, '\0'));
//        board.move(new Move(new Coordinates(6,6), new Coordinates(6, 5), MoveType.ORDINARY, '\0'));
//
//        assertFalse(whitePiece.canMoveAt(new Coordinates(1, 5), board));
//
//        printBoardOnce(board);
//    }

    @Test
    void testPromotion() {
        Coordinates whitePieceCoordinatesToMove = new Coordinates(2,7);
        Coordinates blackPieceCoordinatesToMove = new Coordinates(5,0);
        Coordinates whitePieceCoordinates = new Coordinates(2,6);
        Coordinates blackPieceCoordinates = new Coordinates(5,1);

        Pawn whitePawnPromotion = new Pawn(whitePieceCoordinates, Color.WHITE);
        Pawn blackPawnPromotion = new Pawn(blackPieceCoordinates, Color.BLACK);

        board.setPiece(new Coordinates(2, 7), new Empty(new Coordinates(2, 7), Color.EMPTY));
        board.setPiece(new Coordinates(2, 6), new Empty(new Coordinates(2, 6), Color.EMPTY));
        board.setPiece(new Coordinates(5, 1), new Empty(new Coordinates(5, 1), Color.EMPTY));
        board.setPiece(new Coordinates(5, 0), new Empty(new Coordinates(5, 0), Color.EMPTY));

        board.setPiece(whitePieceCoordinates, whitePawnPromotion);
        board.setPiece(blackPieceCoordinates, blackPawnPromotion);

        printBoardOnce(board);

        assertTrue(whitePawnPromotion.isPromotion(whitePieceCoordinatesToMove, board));
        assertTrue(blackPawnPromotion.isPromotion(blackPieceCoordinatesToMove, board));
    }

    @Test
    public void toStringTest() {
        assertEquals("Pawn: x = 0, y = 1", pawn.toString());
    }
}