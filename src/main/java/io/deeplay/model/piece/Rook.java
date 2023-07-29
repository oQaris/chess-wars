package io.deeplay.model.piece;

import io.deeplay.model.Board;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public List<Integer> getPossibleMoves(Board board) {
        List<Integer> possibleMoves = new ArrayList<>();

        // вправо
        for (int i = x + 1; i < 8; i++) {
            if (canMoveAt(i, y, board)) {
                possibleMoves.add(i * 8 + y);

                if (!board.getBoard()[i][y].getColor().equals(color)) {
                    break;
                }
            } else {
                break;
            }
        }

        // влево
        for (int i = x - 1; i >= 0; i--) {
            if (canMoveAt(i, y, board)) {
                possibleMoves.add(i * 8 + y);
                if (!board.getBoard()[i][y].getColor().equals(color)) {
                    break;
                }
            } else {
                break;
            }
        }

        // вверх
        for (int i = y - 1; i >= 0; i--) {
            if (canMoveAt(x, i, board)) {
                possibleMoves.add(x * 8 + i);
                if (!board.getBoard()[x][i].getColor().equals(color)) {
                    break;
                }
            } else {
                break;
            }
        }

        // вниз
        for (int i = y + 1; i < 8; i++) {
            if (canMoveAt(x, i, board)) {
                possibleMoves.add(x * 8 + i);
                if (!board.getBoard()[x][i].getColor().equals(color)) {
                    break;
                }
            } else {
                break;
            }
        }

        return possibleMoves;
    }

    public boolean canMoveAt(int x, int y, Board board) {
        if (x < 0 || y < 0 || y >= 8 || x >= 8) {
            return false;
        }

        if (this.x == x && this.y == y) {
            return false;
        }

        int xDirection = Integer.compare(x, this.x);
        int yDirection = Integer.compare(y, this.y);

        boolean isFoundOpponentPiece = false;

        if (xDirection != 0 && yDirection != 0) {
            return false;
        }

        int currentX = x + xDirection;
        int currentY = y + yDirection;

        while (currentX != x || currentY != y) {
            if (!board.getBoard()[currentX][currentY].getColor().equals(Color.EMPTY)) {
                if (!board.getBoard()[currentX][currentY].getColor().equals(color)) {
                    return false; // фигура своего цвета
                }

                if (isFoundOpponentPiece) {
                    return false;
                }

                isFoundOpponentPiece = true;
            }

            currentX += xDirection;
            currentY += yDirection;
        }

        return true;
    }
}
