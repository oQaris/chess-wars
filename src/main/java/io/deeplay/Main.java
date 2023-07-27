package io.deeplay;

import io.deeplay.model.Board;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Board board = new Board();

        board.printBoard();
        board.movePiece(48, 40);

        System.out.println();
        System.out.println();
        board.printBoard();
    }
}