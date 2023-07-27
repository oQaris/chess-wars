package io.deeplay.model;

import io.deeplay.model.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private long whitePawns;
    private long whiteKnights;
    private long whiteBishops;
    private long whiteRooks;
    private long whiteQueen;
    private long whiteKing;
    private long blackPawns;
    private long blackKnights;
    private long blackBishops;
    private long blackRooks;
    private long blackQueen;
    private long blackKing;
    private char[] board;

    public Board() {
        whitePawns = 0x00FF000000000000L;
        whiteKnights = 0x4200000000000000L;
        whiteBishops = 0x2400000000000000L;
        whiteRooks = 0x8100000000000000L;
        whiteQueen = 0x0800000000000000L;
        whiteKing = 0x1000000000000000L;

        blackPawns = 0x000000000000FF00L;
        blackKnights = 0x0000000000000042L;
        blackBishops = 0x0000000000000024L;
        blackRooks = 0x0000000000000081L;
        blackQueen = 0x0000000000000008L;
        blackKing = 0x0000000000000010L;

        board = new char[64];

        for (int i = 0; i < 64; i++) {
            board[i] = getPieceAt(i);
        }
    }

    public void printBoard() {
        for (int i = 0; i < 64; i++) {
            System.out.print(board[i] + " ");

            if ((i + 1) % 8 == 0) {
                System.out.println();
            }
        }
    }

    private char getPieceAt(int cell) {
        if ((whitePawns & (1L << cell)) != 0) {
            return 'P';
        } else if ((blackPawns & (1L << cell)) != 0) {
            return 'p';
        } else if ((whiteKnights & (1L << cell)) != 0) {
            return 'N';
        } else if ((blackKnights & (1L << cell)) != 0) {
            return 'n';
        } else if ((whiteBishops & (1L << cell)) != 0) {
            return 'B';
        } else if ((blackBishops & (1L << cell)) != 0) {
            return 'b';
        } else if ((whiteRooks & (1L << cell)) != 0) {
            return 'R';
        } else if ((blackRooks & (1L << cell)) != 0) {
            return 'r';
        } else if ((whiteQueen & (1L << cell)) != 0) {
            return 'Q';
        } else if ((blackQueen & (1L << cell)) != 0) {
            return 'q';
        } else if ((whiteKing & (1L << cell)) != 0) {
            return 'K';
        } else if ((blackKing & (1L << cell)) != 0) {
            return 'k';
        } else {
            return '_';
        }
    }

    public List<Move> getAllPossibleMoves() {
        return new ArrayList<>();
    }

    private void updateBoard() {
        for (int i = 0; i < 64; i++) {
            board[i] = getPieceAt(i);
        }
    }

    public void movePiece(int from, int to) {
        whitePawns &= ~(1L << from);
        whitePawns |= (1L << to);
        updateBoard();
    }
}

