package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {
    Board board;
    King king;

    @BeforeEach
    void setUp() {
        board = new Board();
        king = new King(new Coordinates(0,5), Color.WHITE);
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, board.getPiece(new Coordinates(4, 0)).getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(4, 7)).getColor());
    }

    @Test
    void getPossibleMovesFromStartBoard() {
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(4, 0)).getPossibleMoves(board);

        Assertions.assertEquals(0, possibleMoves.size());
    }

    @Test
    void getPossibleMovesFromCenter(){
        List<Coordinates> possibleMovesFromCenter = king.getPossibleMoves(board);
        Assertions.assertEquals(5, possibleMovesFromCenter.size());
    }

    @Test
    void canMoveToEmptyCell() {
        List<Coordinates> possibleMovesFromCenter = king.getPossibleMoves(board);
        assertTrue(possibleMovesFromCenter.contains(new Coordinates(1, 5)));
    }

    @Test
    void canMoveToCellWithAllyPiece() {
        board.setPiece(new Coordinates(0,4), new King(new Coordinates(0,4), Color.WHITE));
        assertFalse(king.canMoveAt(new Coordinates(0, 4), board));
    }

    @Test
    void canMoveAtEnemyPiece(){
        assertTrue(king.canMoveAt(new Coordinates(0,6), board));
    }

    @Test
    void canMoveOffTheBoard(){
        assertFalse(king.canMoveAt(new Coordinates(-1, 5), board));
    }

    @Test
    void canMoveOnCurrent(){
        assertFalse(king.canMoveAt(new Coordinates(0,5),board));
    }

    @Test
    public void toStringTest() {
        assertEquals("King: x = 0 y = 5", king.toString());
    }
}