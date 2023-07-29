package io.deeplay;

import io.deeplay.model.Board;
import io.deeplay.model.piece.*;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();

        System.out.println(board.getPiece(0, 0).getColor());
        System.out.println(board.getPiece(0, 0).getClass());
        System.out.println(board.getPiece(2, 0).getColor());
        System.out.println(board.getPiece(2, 0).getClass());
        System.out.println(board.getPiece(1,0).getColor());
        System.out.println(board.getPiece(1, 0).getClass());
        System.out.println(board.getPiece(0, 0).getPossibleMoves(board));
        System.out.println(board.getPiece(0, 0).canMoveAt(1, 0, board));
    }
}
