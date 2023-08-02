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
        if (pieceToMove != null) {
            availableMoveSquares.addAll(pieceToMove.getPossibleMoves(board));
        }

        for (int i = 7; i >= 0; i--) {
            String line = "";
            for (int j = 0; j < 8; j++) {
                assert availableMoveSquares != null;
                boolean isHighlight = availableMoveSquares.contains(new Coordinates(j, i));
                if (isSquareEmpty(board, new Coordinates(j, i))) {
                    line += getSpriteForEmptySquare(new Coordinates(j, i), isHighlight);
                } else {
                    line += getPieceSprite(board.getPiece(new Coordinates(j, i)), isHighlight);
                }
            }
            line += ANSI_RESET;
            System.out.println(line);
        }
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
    }

    private static String colorizeSprite(String sprite, Color pieceColor, boolean isSquareDark, boolean isHighlight) {
        // format = background color + font color + text
        String result = sprite;
        if (pieceColor == Color.WHITE) {
            result = ANSI_WHITE_PIECE_COLOR + result;
        } else if (pieceColor == Color.BLACK) {
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
        return colorizeSprite("\u2002" + "\u2005\u205F" +  "\u2005\u2005", Color.EMPTY, isSquareDark(coordinates), isHighlight);
    }

    private static boolean isSquareDark(Coordinates coordinates) {
        return (coordinates.getX() + coordinates.getY()) % 2 == 1;
    }

    private boolean isSquareEmpty(Board board, Coordinates coordinates) {
        return board.getPiece(coordinates) instanceof Empty;
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
                 "\u2005" + selectUnicodeSpriteForPiece(piece) + "\u2005", piece.getColor(), isSquareDark(piece.getCoordinates()),
                isHighlight
        );
    }
}
