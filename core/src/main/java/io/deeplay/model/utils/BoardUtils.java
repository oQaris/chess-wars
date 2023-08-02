package io.deeplay.model.utils;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.*;

import java.util.ArrayList;
import java.util.List;


public class BoardUtils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE_PIECE_COLOR = "\u001B[97m";
    public static final String ANSI_BLACK_PIECE_COLOR = "\u001B[30m";
    public static final String ANSI_WHITE_SQUARE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_BLACK_SQUARE_BACKGROUND = "\u001B[0;100m";
    public static final String ANSI_HIGHLIGHTED_SQUARE_BACKGROUND = "\u001B[45m";

    public void render(Board board, Piece pieceToMove) {
        List<Coordinates> availableMoveSquares = new ArrayList<>();
        switch(pieceToMove.getClass().getSimpleName()){
            case "Pawn":
                pieceToMove = new Pawn(pieceToMove.getCoordinates(), pieceToMove.getColor());
                break;
            case "Knight":
                pieceToMove = new Knight(pieceToMove.getCoordinates(), pieceToMove.getColor());
                break;
            case "Bishop":
                pieceToMove = new Bishop(pieceToMove.getCoordinates(), pieceToMove.getColor());
                break;
            case "Rook":
                pieceToMove = new Rook(pieceToMove.getCoordinates(), pieceToMove.getColor());
                break;
            case "Queen":
                pieceToMove = new Queen(pieceToMove.getCoordinates(), pieceToMove.getColor());
                break;
            case "King":
                pieceToMove = new King(pieceToMove.getCoordinates(), pieceToMove.getColor());
                break;
            case "Empty":
                pieceToMove = new Empty(pieceToMove.getCoordinates(), pieceToMove.getColor());
                break;
        }

        if (pieceToMove != null) {
            availableMoveSquares.addAll(pieceToMove.getPossibleMoves(board));
        }

        for (int i = 7; i >= 0; i--) {
            String line = "";
            for (int j = 0; j < 8; j++) {
                assert availableMoveSquares != null;
                boolean isHighlight = availableMoveSquares.contains(new Coordinates(i, j));
                if (isSquareEmpty(board, new Coordinates(i, j))) {
                    line += "   " + getSpriteForEmptySquare(new Coordinates(i,j), isHighlight);
                } else {
                    line += getPieceSprite(board.getPiece(new Coordinates(i,j)), isHighlight);
                }
            }
            line += ANSI_RESET;
            System.out.println(line);
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
    }

    public void render(Board board) {
        render(board, null);
    }

    private static String colorizeSprite(String sprite, Color pieceColor, boolean isSquareDark, boolean isHighlight) {
        // format = background color + font color + text
        String result = sprite;
        if (pieceColor == Color.WHITE) {
            result = ANSI_WHITE_PIECE_COLOR + result;
        } else {
            result = ANSI_BLACK_PIECE_COLOR + result;
        }
        if (isHighlight) {
            result = ANSI_HIGHLIGHTED_SQUARE_BACKGROUND + result;
        } else if (isSquareDark) {
            result = ANSI_BLACK_SQUARE_BACKGROUND + result;
        } else {
            result = ANSI_WHITE_SQUARE_BACKGROUND + result;
        }
        return result;
    }

    private String getSpriteForEmptySquare(Coordinates coordinates, boolean isHighlight) {
        return colorizeSprite("  ", Color.WHITE, isSquareDark(coordinates), isHighlight);
    }

    private static boolean isSquareDark(Coordinates coordinates) {
        return (coordinates.getX() + coordinates.getY()) % 2 == 1;
    }

    private boolean isSquareEmpty(Board board, Coordinates coordinates) {
        return board.getPiece(coordinates) == null;
    }

    private static String selectUnicodeSpriteForPiece(Piece piece) {
        return switch (piece.getClass().getSimpleName()) {
            case "Pawn" -> "♟";
            case "Knight" -> "♞";
            case "Bishop" -> "♝";
            case "Rook" -> "♜";
            case "Queen" -> "♛";
            case "King" -> "♚";
            default -> "";
        };
    }

    private static String getPieceSprite(Piece piece, boolean isHighlight) {
        return colorizeSprite(
                " " + selectUnicodeSpriteForPiece(piece) + "  ", piece.getColor(), isSquareDark(piece.getCoordinates()),
                isHighlight
        );
    }
}
