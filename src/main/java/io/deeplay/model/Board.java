package io.deeplay.model;

import io.deeplay.model.piece.*;

public class Board {
    private Piece[][] board;

    public Board() {
        board = new Piece[8][8];
        board[0][0] = new Rook(0, 0, Color.WHITE);
        board[0][1] = new Knight(0, 1, Color.WHITE);
        board[0][2] = new Bishop(0, 2, Color.WHITE);
        board[0][3] = new Queen(0, 3, Color.WHITE);
        board[0][4] = new King(0, 4, Color.WHITE);
        board[0][5] = new Bishop(0, 5, Color.WHITE);
        board[0][6] = new Knight(0, 6, Color.WHITE);
        board[0][7] = new Rook(0, 7, Color.WHITE);

        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(1, i, Color.WHITE);
        }

        board[7][0] = new Rook(7, 0, Color.BLACK);
        board[7][1] = new Knight(7, 1, Color.BLACK);
        board[7][2] = new Bishop(7, 2, Color.BLACK);
        board[7][3] = new Queen(7, 3, Color.BLACK);
        board[7][4] = new King(7, 4, Color.BLACK);
        board[7][5] = new Bishop(7, 5, Color.BLACK);
        board[7][6] = new Knight(7, 6, Color.BLACK);
        board[7][7] = new Rook(7, 7, Color.BLACK);

        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(6, i, Color.BLACK);
        }
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public void setPiece(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }
}