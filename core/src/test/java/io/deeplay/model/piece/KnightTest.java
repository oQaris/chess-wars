package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {
    private Board board;
    private Knight knight;
    @BeforeEach
    void setUp() {
        board = new Board();
        knight = new Knight(new Coordinates(4,5), Color.WHITE);
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, board.getPiece(new Coordinates(1, 0)).getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(1, 7)).getColor());
    }

    @Test
    void getPossibleMovesFromStartBoard() {
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(1, 0)).getPossibleMoves(board);

        Assertions.assertEquals(2, possibleMoves.size());
    }

    @Test
    void getPossibleMovesFromCenter(){
        List<Coordinates> possibleMovesFromCenter = knight.getPossibleMoves(board);
        Assertions.assertEquals(8, possibleMovesFromCenter.size());
    }

    @Test
    void canMoveToEmptyCell() {
        List<Coordinates> possibleMovesFromCenter = knight.getPossibleMoves(board);
        assertTrue(possibleMovesFromCenter.contains(new Coordinates(2, 4)));
    }

    @Test
    void canMoveToCellWithAllyPiece() {
        Coordinates coordinates = new Coordinates(5,3);
        board.setPiece(coordinates, new Rook(coordinates, Color.WHITE));
        assertFalse(knight.canMoveAt(new Coordinates(5, 3), board));
    }

    @Test
    void canMoveAtEnemyPiece(){
        assertTrue(knight.canMoveAt(new Coordinates(5,7), board));
    }

    @Test
    void canMoveOnCurrent(){
        assertFalse(knight.canMoveAt(new Coordinates(4,5),board));
    }

    @Test
    public void toStringTest() {
        assertEquals("Knight: x = 4, y = 5", knight.toString());
    }
}