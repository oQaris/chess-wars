package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    private Board board;
    private Bishop bishop;

    @BeforeEach
    void setUp() {
        board = new Board();
        bishop = new Bishop(new Coordinates(3, 3), Color.WHITE);
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, board.getPiece(new Coordinates(2, 0)).getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(2, 7)).getColor());
    }

    @Test
    void getPossibleMoves() {
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(0, 0)).getPossibleMoves(board);
        Assertions.assertEquals(0, possibleMoves.size());

        List<Coordinates> possibleMovesFromCenter = bishop.getPossibleMoves(board);
        Assertions.assertEquals(9, possibleMovesFromCenter.size());
    }

    @Test
    public void canMoveToEmptyCell() {
        List<Coordinates> possibleMovesFromCenter = bishop.getPossibleMoves(board);
        assertTrue(possibleMovesFromCenter.contains(new Coordinates(4, 4)));
    }

    @Test
    public void canMoveToCellWithAllyPiece() {
        board.setPiece(new Coordinates(4,4), new Bishop(new Coordinates(4,4), Color.WHITE));
        assertFalse(bishop.canMoveAt(new Coordinates(4, 4), board));
    }

    @Test
    public void canMoveAtEnemyPiece(){
        assertTrue(bishop.canMoveAt(new Coordinates(0,6), board));
    }

    @Test
    public void canMoveOffTheBoard(){
        assertFalse(bishop.canMoveAt(new Coordinates(-1, -1), board));
        assertFalse(bishop.canMoveAt(new Coordinates(8, 8), board));
    }

    @Test
    void canMoveAt() {
        Assertions.assertFalse(board.getPiece(new Coordinates(0, 0)).canMoveAt(new Coordinates(-1, 0), board));
        Assertions.assertFalse(board.getPiece(new Coordinates(0, 0)).canMoveAt(new Coordinates(0, 0), board));

        Assertions.assertFalse(board.getPiece(new Coordinates(0, 0)).canMoveAt(new Coordinates(2, 0), board));
        Assertions.assertFalse(board.getPiece(new Coordinates(0, 0)).canMoveAt(new Coordinates(1, 1), board));
    }

    @Test
    public void toStringTest() {
        assertEquals("Bishop: x = 3 y = 3", bishop.toString());
    }
}