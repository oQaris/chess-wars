package io.deeplay.model.piece;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {
    Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, board.getPiece(new Coordinates(4, 0)).getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(4, 7)).getColor());
    }

    @Test
    void getPossibleMoves() {
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(0, 0)).getPossibleMoves(board);

        Assertions.assertEquals(0, possibleMoves.size());
    }

    @Test
    void canMoveAt() {
        Assertions.assertFalse(board.getPiece(new Coordinates(4, 0)).canMoveAt(new Coordinates(-1, 0), board));
        Assertions.assertFalse(board.getPiece(new Coordinates(4, 0)).canMoveAt(new Coordinates(0, 0), board));

        Assertions.assertFalse(board.getPiece(new Coordinates(4, 0)).canMoveAt(new Coordinates(4, 1), board));
    }
}