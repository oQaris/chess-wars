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
import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private Board board;
    private Piece pawn;

    @BeforeEach
    void setUp() {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        board = new Board();
        pawn = board.getPiece(new Coordinates(0, 1));
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, pawn.getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(0, 6)).getColor());
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