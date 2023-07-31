package io.deeplay.model;

import io.deeplay.model.piece.*;

public class Board {
    private Piece[][] board;

    public Board() {
        board = getStartBoard();
    }

    public Piece[][] getStartBoard() {
        board = new Piece[8][8];

        board[0][0] = new Rook(new Coordinates(0, 0), Color.WHITE);
        board[1][0] = new Knight(new Coordinates(1, 0), Color.WHITE);
        board[2][0] = new Bishop(new Coordinates(2, 0), Color.WHITE);
        board[3][0] = new Queen(new Coordinates(3, 0), Color.WHITE);
        board[4][0] = new King(new Coordinates(4, 0), Color.WHITE);
        board[5][0] = new Bishop(new Coordinates(5, 0), Color.WHITE);
        board[6][0] = new Knight(new Coordinates(6, 0), Color.WHITE);
        board[7][0] = new Rook(new Coordinates(7, 0), Color.WHITE);

        board[0][7] = new Rook(new Coordinates(0, 7), Color.BLACK);
        board[1][7] = new Knight(new Coordinates(1, 7), Color.BLACK);
        board[2][7] = new Bishop(new Coordinates(2, 7), Color.BLACK);
        board[3][7] = new Queen(new Coordinates(3, 7), Color.BLACK);
        board[4][7] = new King(new Coordinates(4, 7), Color.BLACK);
        board[5][7] = new Bishop(new Coordinates(5, 7), Color.BLACK);
        board[6][7] = new Knight(new Coordinates(6, 7), Color.BLACK);
        board[7][7] = new Rook(new Coordinates(7, 7), Color.BLACK);

        for (int i = 0; i < 8; i++) {
            board[i][6] = new Pawn(new Coordinates(i, 6), Color.BLACK);
            board[i][1] = new Empty(new Coordinates(i, 1), Color.WHITE);
        }

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[j][i] = new Empty(new Coordinates(j, i), Color.EMPTY);
            }
        }

        return board;
    }

    public Piece getPiece(Coordinates coordinates) {
        return board[coordinates.getX()][coordinates.getY()];
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setPiece(Coordinates coordinates, Piece piece) {
        board[coordinates.getX()][coordinates.getY()] = piece;
    }
}