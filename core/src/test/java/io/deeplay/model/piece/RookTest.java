package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {
    private Board board;
    private Rook rook;

    @BeforeEach
    void setUp() {
        board = new Board();
        rook = new Rook(new Coordinates(4,4), Color.WHITE);
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, board.getPiece(new Coordinates(0, 0)).getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(7, 7)).getColor());
    }

    @Test
    void getPossibleMovesFromStartBoard() {
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(0, 0)).getPossibleMoves(board);

        Assertions.assertEquals(0, possibleMoves.size());
    }

    @Test
    void getPossibleMovesFromCenter(){
        List<Coordinates> possibleMovesFromCenter = rook.getPossibleMoves(board);
        Assertions.assertEquals(11, possibleMovesFromCenter.size());
    }

    @Test
    void canMoveToEmptyCell() {
        assertTrue(rook.canMoveAt(new Coordinates(4, 2), board));
    }

    @Test
    void canMoveToCellWithAllyPiece() {
        assertFalse(rook.canMoveAt(new Coordinates(4, 1), board));
    }

    @Test
    void canMoveAtEnemyPiece(){
        assertTrue(rook.canMoveAt(new Coordinates(4,6), board));
    }

    @Test
    void canMoveOnCurrent(){
        assertFalse(rook.canMoveAt(new Coordinates(4,4),board));
    }

    @Test
    public void toStringTest() {
        assertEquals("Rook: x = 4 y = 4", rook.toString());
    }
}