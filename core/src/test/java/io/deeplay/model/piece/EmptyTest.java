package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmptyTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void getPossibleMoves() {
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(3, 3)).getPossibleMoves(board);

        Assertions.assertEquals(0, possibleMoves.size());
    }

    @Test
    void canMoveAt() {
        Assertions.assertFalse(board.getPiece(new Coordinates(3, 3)).canMoveAt(new Coordinates(2, 0), board));
    }

    @Test
    public void getColor() {
        assertEquals(Color.EMPTY, board.getPiece(new Coordinates(3, 3)).getColor());
    }

    @Test
    public void toStringTest() {
        Empty empty = new Empty(new Coordinates(3, 3));
        assertEquals("Empty: x = 3 y = 3", empty.toString());
    }
}