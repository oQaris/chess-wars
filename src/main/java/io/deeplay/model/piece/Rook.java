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

        for (int i = 0; i < 8; i++) {
            if (canMoveAt(x, i, board)) {
                possibleMoves.add((x * 8) + i);
            }

            if (canMoveAt(i, y, board)) {
                possibleMoves.add((i * 8) + y);
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

        if (board.getBoard()[x][y].getColor().equals(color)) { // фигура того же цвета
            return false;
        }

        int xDirection = Integer.compare(x, this.x);
        int yDirection = Integer.compare(y, this.y);

        int currentX = this.x + xDirection;
        int currentY = this.y + yDirection;

        while (currentX != x || currentY != y) {
            if (!board.getBoard()[currentX][currentY].getColor().equals(Color.EMPTY)) { // если не пустая, на пути стоит фигура другого цвета
                return false;
            }

            currentX += xDirection;
            currentY += yDirection;
        }

        return true;
    }
}
