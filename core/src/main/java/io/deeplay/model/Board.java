package io.deeplay.model;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveHistory;
import io.deeplay.model.piece.*;
import io.deeplay.model.utils.BoardUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс Board представляет шахматную доску.
 * Он хранит состояние шахматной доски, включая положения фигур,
 * количество оставшихся черных и белых фигур, и историю ходов.
 */
public class Board {
    private Piece[][] board;
    private final boolean[][] pieceMoved;
    public static final int BOARD_LENGTH = 8;
    public static final int BOARD_HEIGHT = 8;
    private int blackPiecesNumber = 16;
    private int whitePiecesNumber = 16;
    private static final Logger logger = LogManager.getLogger(Board.class);

    private final MoveHistory moveHistory = new MoveHistory();

    public Board() {
        board = getStartBoard();
        pieceMoved = new boolean[8][8];
    }

    /**
     * Возвращает начальную конфигурацию шахматной доски.
     *
     * @return двумерный массив, представляющий начальную конфигурацию шахматной доски.
     */
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

    public Piece[][] getEmptyBoard() {
        Piece[][] newEmptyBoard = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newEmptyBoard[i][j] = new Empty(new Coordinates(i, j));
            }
        }

        return newEmptyBoard;
    }

    /**
     * Метод для выполнения хода на доске.
     *
     * @param move ход, который нужно выполнить.
     */
    public void move(Move move) {
        Coordinates start = move.startPosition();
        Coordinates end = move.endPosition();
        MoveType moveType = move.moveType();

        Piece pieceToMove = board[start.getX()][start.getY()];
        Piece pieceToRemove = board[end.getX()][end.getY()];

        if (pieceToMove instanceof King || pieceToMove instanceof Rook) {
            pieceMoved[start.getX()][start.getY()] = true;
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

            logger.info("Игрок сделал обычный ход");
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

            logger.info("Игрок сделал рокировку");
        } else if (moveType == MoveType.EN_PASSANT) {
            if (pieceToMove.getColor() == Color.WHITE) {
                blackPiecesNumber--;
            } else {
                whitePiecesNumber--;
            }

            board[end.getX()][start.getY()] = new Empty(new Coordinates(end.getX(), start.getY()));
            board[end.getX()][end.getY()] = new Pawn(new Coordinates(end.getX(), end.getY()), pieceToMove.getColor());
            board[start.getX()][start.getY()] = new Empty(new Coordinates(start.getX(), start.getY()));

            logger.info("Игрок сделал взятие на проходе");
        } else if (moveType == MoveType.PROMOTION) {
            Piece newPiece = null;
            switch (move.switchPieceType()) {
                case BISHOP -> newPiece = new Bishop(end, pieceToMove.getColor());
                case KNIGHT -> newPiece = new Knight(end, pieceToMove.getColor());
                case QUEEN -> newPiece = new Queen(end, pieceToMove.getColor());
                case ROOK -> newPiece = new Rook(end, pieceToMove.getColor());
                default -> throw new IllegalArgumentException("Invalid choice");
            }

            if (!pieceToRemoveColor.equals(Color.EMPTY)) {
                if (pieceToRemoveColor.equals(Color.BLACK)) {
                    blackPiecesNumber--;
                } else {
                    whitePiecesNumber--;
                }
            }

            board[end.getX()][end.getY()] = newPiece;
            board[start.getX()][start.getY()] = new Empty(start);

            logger.info("Игрок сделал promotion");
        }
    }

        moveHistory.addMove(move);
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