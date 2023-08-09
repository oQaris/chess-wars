package io.deeplay.model;

import io.deeplay.domain.Color;
import io.deeplay.model.move.Move;
import io.deeplay.domain.MoveType;
import io.deeplay.model.move.MoveHistory;
import io.deeplay.model.piece.*;
import io.deeplay.model.utils.BoardUtils;

import java.util.Scanner;

public class Board {
    private static Piece[][] board;
    private boolean[][] pieceMoved;
    private int blackPiecesNumber = 16;
    private int whitePiecesNumber = 16;

    private final MoveHistory moveHistory = new MoveHistory();

    public Board() {
        board = getStartBoard();
        pieceMoved = new boolean[8][8];
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
                board[j][i] = new Empty(new Coordinates(j, i));
            }
        }

        return board;
    }

    public static void printBoard(Board board) {
        BoardUtils boardUtils = new BoardUtils();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardUtils.render(board, board.getPiece(new Coordinates(i, j)));
            }
        }
    }

    public static void printBoard(Board board, Piece piece) {
        BoardUtils boardUtils = new BoardUtils();
        boardUtils.render(board, board.getPiece(piece.getCoordinates()));
    }

    public static void printBoardOnce(Board board) {
        BoardUtils boardUtils = new BoardUtils();
        boardUtils.render(board);
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
                board[i][j] = new Empty(new Coordinates(i, j));
            }
        }

        return board;
    }

    public void move(Move move) {
        Coordinates start = move.startPosition();
        Coordinates end = move.endPosition();
        MoveType moveType = move.moveType();

        Piece pieceToMove = board[start.getX()][start.getY()];
        Piece pieceToRemove = board[end.getX()][end.getY()];

        if (pieceToMove instanceof King || pieceToMove instanceof Rook) pieceMoved[start.getX()][start.getY()] = true;

        if (pieceToMove.getColor().equals(Color.EMPTY)) {
            return;
        }

        if (!pieceToMove.canMoveAt(end, this) && moveType != MoveType.CASTLING) {
            return;
        }

        Color pieceToRemoveColor = pieceToRemove.getColor();

        if (moveType == MoveType.ORDINARY || moveType == MoveType.TAKE) {
            if (!pieceToRemoveColor.equals(Color.EMPTY)) {
                if (pieceToRemoveColor.equals(Color.BLACK)) {
                    blackPiecesNumber--;
                } else {
                    whitePiecesNumber--;
                }
            }

            board[end.getX()][end.getY()] = pieceToMove;
            board[start.getX()][start.getY()] = new Empty(start);
            pieceToMove.setCoordinates(end);
        } else if (moveType == MoveType.CASTLING) {
            Piece rookToMove = null;
            int moveSide = start.getX() - end.getX();

            if (moveSide == 2) {
                rookToMove = getPiece(new Coordinates(0, start.getY()));
                board[3][start.getY()] = rookToMove;
                board[0][start.getY()] = new Empty(rookToMove.getCoordinates());
                rookToMove.setCoordinates(new Coordinates(3, start.getY()));

                board[end.getX()][end.getY()] = pieceToMove;
                board[start.getX()][start.getY()] = new Empty(start);
                pieceToMove.setCoordinates(end);
            }

            if (moveSide == -2) {
                rookToMove = getPiece(new Coordinates(7, start.getY()));
                board[5][start.getY()] = rookToMove;
                board[7][start.getY()] = new Empty(rookToMove.getCoordinates());
                rookToMove.setCoordinates(new Coordinates(5, start.getY()));

                board[end.getX()][end.getY()] = pieceToMove;
                board[start.getX()][start.getY()] = new Empty(start);
                pieceToMove.setCoordinates(end);
            }
        } else if (moveType == MoveType.EN_PASSANT) {
            if (pieceToMove.getColor() == Color.WHITE) {
                blackPiecesNumber--;
            } else {
                whitePiecesNumber--;
            }

            board[end.getX()][start.getY()] = new Empty(new Coordinates(end.getX(), end.getY()));
            board[end.getX()][end.getY()] = pieceToMove;
            board[start.getX()][start.getY()] = new Empty(start);
        } else if (moveType == MoveType.PROMOTION) {
            Piece newPiece = choosePromotionPiece(end, pieceToMove);

            if (!pieceToRemoveColor.equals(Color.EMPTY)) {
                if (pieceToRemoveColor.equals(Color.BLACK)) {
                    blackPiecesNumber--;
                } else {
                    whitePiecesNumber--;
                }
            }

            board[end.getX()][end.getY()] = newPiece;
            board[start.getX()][start.getY()] = new Empty(start);
        }

        moveHistory.addMove(move);
    }

    private Piece choosePromotionPiece(Coordinates endCoordinates, Piece pieceToMove) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите новую фигуру: ");
        System.out.println("1. Queen");
        System.out.println("2. Rook");
        System.out.println("3. Bishop");
        System.out.println("4. Knight");
        int choice = scanner.nextInt();

        return switch (choice) {
            case 1 -> new Queen(endCoordinates, pieceToMove.getColor());
            case 2 -> new Rook(endCoordinates, pieceToMove.getColor());
            case 3 -> new Bishop(endCoordinates, pieceToMove.getColor());
            case 4 -> new Knight(endCoordinates, pieceToMove.getColor());
            default -> throw new IllegalArgumentException("Invalid choice: " + choice);
        };
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    public int getBlackPiecesNumber() {
        return blackPiecesNumber;
    }

    public int getWhitePiecesNumber() {
        return whitePiecesNumber;
    }

    public boolean[][] getPieceMoved() {
        return pieceMoved;
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
    }
}