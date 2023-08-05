package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

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







    @Test
    void getPossibleMovesFromStartBoard() {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        List<Coordinates> possibleMoves = pawn.getPossibleMoves(board);
        printBoard(board, pawn);
        Assertions.assertEquals(2, possibleMoves.size());
    }

    @Test
    void canMoveToEmptyCell() {
        assertTrue(pawn.canMoveAt(new Coordinates(0, 2), board));
    }

    @Test
    void canMoveThrough2Squares() {
        assertTrue(pawn.canMoveAt(new Coordinates(0, 3), board));
    }



    @Test
    void canMoveToCellWithAllyPiece() {
        Coordinates coordinates = new Coordinates(0, 2);
        board.setPiece(coordinates, new Pawn(coordinates, Color.WHITE));
        assertFalse(pawn.canMoveAt(coordinates, board));
    }

    @Test
    void canAttackEnemyPiece() {
        Coordinates coordinates = new Coordinates(1, 2);
        board.setPiece(coordinates, new Pawn(coordinates, Color.BLACK));
        assertTrue(pawn.canMoveAt(coordinates, board));
    }

    @Test
    void canAttackEnemyPieceBlack() {
        Coordinates coordinates = new Coordinates(2, 2);
        board.setPiece(coordinates, new Pawn(coordinates, Color.BLACK));
        printBoard(board, board.getPiece(coordinates));
        assertTrue(board.getPiece(new Coordinates(1, 1)).canMoveAt(coordinates, board));
    }

    @Test
    void canMoveBlackPawn() {
        printBoard(board, board.getPiece(new Coordinates(1, 6)));
        assertEquals(2, board.getPiece(new Coordinates(1, 6)).getPossibleMoves(board).size());
    }

    @Test
    void canMoveOffTheBoard() {
        assertFalse(pawn.canMoveAt(new Coordinates(-1, 2), board));
    }

    @Test
    void canMoveOnCurrent() {
        assertFalse(pawn.canMoveAt(new Coordinates(0, 1), board));
    }

    @Test
    public void toStringTest() {
        assertEquals("Pawn: x = 0, y = 1", pawn.toString());
    }
}