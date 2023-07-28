package io.deeplay.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @org.junit.jupiter.api.Test
    void printBoard() {
    }

    @Test
    public void testGetPieceAt() {
        Board board = new Board();
        assertEquals('r', board.getPieceAt(0));
        assertEquals('p', board.getPieceAt(8));
        assertEquals('_', board.getPieceAt(16));
        assertEquals('K', board.getPieceAt(60));
        assertEquals('R', board.getPieceAt(63));
    }
}