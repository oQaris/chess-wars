package io.deeplay.model;

import io.deeplay.domain.Color;
import io.deeplay.model.move.Move;
import io.deeplay.domain.MoveType;
import io.deeplay.model.piece.*;
import io.deeplay.model.utils.BoardUtils;

public class Board {
    private static Piece[][] board;
    private int blackPiecesNumber = 16;
    private int whitePiecesNumber = 16;

    public Board() {
        board = getStartBoard();
    }

    public static Piece[][] getStartBoard() {
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
            board[i][1] = new Pawn(new Coordinates(i, 1), Color.WHITE);
        }

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[j][i] = new Empty(new Coordinates(j, i), Color.EMPTY);
            }
        }

        return board;
    }
    public static void printBoard(Board board){
        BoardUtils boardUtils = new BoardUtils();
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++){
                boardUtils.render(board, board.getPiece(new Coordinates(i,j)));
            }
        }
    }

    public Piece getPiece(Coordinates coordinates) {
        return board[coordinates.getX()][coordinates.getY()];
    }

    public void setPiece(Coordinates coordinates, Piece piece) {
        board[coordinates.getX()][coordinates.getY()] = piece;
    }

    public static Piece[][] getEmptyBoard() {
        board = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Empty(new Coordinates(i, j), Color.EMPTY);
            }
        }

        return board;
    }

    public void move(Move move) {
        Coordinates start = move.getStartPosition();
        Coordinates end = move.getEndPosition();
        MoveType moveType = move.getMoveType();

        Piece pieceToMove = board[start.getX()][start.getY()];
        Piece pieceToRemove = board[end.getX()][end.getY()];

        if (pieceToMove.getColor().equals(Color.EMPTY)) {
            return;
        }

        if (!pieceToMove.canMoveAt(end, this)) {
            return;
        }

        Color pieceToRemoveColor = pieceToRemove.getColor();

        if (moveType == MoveType.ORDINARY) {
            if (!pieceToRemoveColor.equals(Color.EMPTY)) {   // сруб фигуры (подсчет оставшихся фигур??)
                if (pieceToRemoveColor.equals(Color.BLACK)) {
                    blackPiecesNumber--;
                } else {
                    whitePiecesNumber--;
                }
            }

            board[end.getX()][end.getY()] = pieceToMove;
            board[start.getX()][start.getY()] = new Empty(start, Color.EMPTY);
            pieceToMove.setCoordinates(end);
        } else if (moveType == MoveType.CASTLING) {
            // обработка рокировки
        } else if (moveType == MoveType.EN_PASSANT) {
            // обработка взятия на проходе
        } else if (moveType == MoveType.PROMOTION) {
            // обработка promotion
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }
}