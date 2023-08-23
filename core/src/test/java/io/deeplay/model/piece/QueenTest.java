package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {
    Board board;
    Queen queen;

    @BeforeEach
    void setUp() {
        board = new Board();
        queen = new Queen(new Coordinates(4,4), Color.WHITE);
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, board.getPiece(new Coordinates(3, 0)).getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(3, 7)).getColor());
    }

    @Test
    void getPossibleMovesFromStartBoard() {
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(3, 0)).getPossibleMoves(board);

        Assertions.assertEquals(0, possibleMoves.size());
    }

    @Test
    void getPossibleMovesFromCenter(){
        List<Coordinates> possibleMovesFromCenter = queen.getPossibleMoves(board);
        Assertions.assertEquals(19, possibleMovesFromCenter.size());
    }

    @Test
    void canMoveToEmptyCell() {
        assertTrue(queen.canMoveAt(new Coordinates(4, 2), board));
    }

    @Test
    void canMoveToCellWithAllyPiece() {
        assertFalse(queen.canMoveAt(new Coordinates(4, 1), board));
    }

    @Test
    void canMoveAtEnemyPieceStraight(){
        assertTrue(queen.canMoveAt(new Coordinates(4,6), board));
    }

    @Test
    void canMoveAtEnemyPieceDiagonal(){
        assertTrue(queen.canMoveAt(new Coordinates(6,6), board));
    }

    @Test
    void canMoveOnCurrentCell(){
        assertFalse(queen.canMoveAt(new Coordinates(4,4),board));
    }

    @Test
    public void toStringTest() {
        assertEquals("Queen: x = 4, y = 4", queen.toString());
    }
}