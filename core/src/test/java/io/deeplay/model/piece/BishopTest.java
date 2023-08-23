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
    void getPossibleMovesFromStartBoard() {
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(2, 0)).getPossibleMoves(board);
        Assertions.assertEquals(0, possibleMoves.size());
    }

    @Test
    void getPossibleMovesFromCenter(){
        List<Coordinates> possibleMovesFromCenter = bishop.getPossibleMoves(board);
        Assertions.assertEquals(8, possibleMovesFromCenter.size());
    }

    @Test
    void canMoveToEmptyCell() {
        List<Coordinates> possibleMovesFromCenter = bishop.getPossibleMoves(board);
        assertTrue(possibleMovesFromCenter.contains(new Coordinates(4, 4)));
    }

    @Test
    void canMoveToCellWithAllyPiece() {
        board.setPiece(new Coordinates(4,4), new Bishop(new Coordinates(4,4), Color.WHITE));
        assertFalse(bishop.canMoveAt(new Coordinates(4, 4), board));
    }

    @Test
    void canMoveAtEnemyPiece(){
        assertTrue(bishop.canMoveAt(new Coordinates(0,6), board));
    }

    @Test
    void canMoveOnCurrent(){
        assertFalse(bishop.canMoveAt(new Coordinates(3,3),board));
    }

    @Test
    public void toStringTest() {
        assertEquals("Bishop: x = 3 y = 3", bishop.toString());
    }
}