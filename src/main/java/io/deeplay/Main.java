package io.deeplay;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.service.PieceService;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();

      //  System.out.println(board.getPiece(new Coordinates(0, 0)));
     //   System.out.println(board.getPiece(new Coordinates(0, 0)).getPossibleMoves(board));
      //  System.out.println(board.getPiece(new Coordinates(0, 0)).canMoveAt(new Coordinates(2, 2), board));

      //  System.out.println(board.getPiece(new Coordinates(4, 0)));
      //  System.out.println(board.getPiece(new Coordinates(4, 0)).getPossibleMoves(board));
      //  System.out.println(board.getPiece(new Coordinates(4, 0)).canMoveAt(new Coordinates(5, 1), board));

     //   System.out.println(board.getPiece(new Coordinates(5, 0)));
      //  System.out.println(board.getPiece(new Coordinates(5, 0)).getPossibleMoves(board));

        System.out.println(PieceService.getPiecesPossibleToMove(board));
    }
}
