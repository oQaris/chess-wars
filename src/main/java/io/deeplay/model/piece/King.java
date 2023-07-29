package io.deeplay.model.piece;

import io.deeplay.model.Board;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(int x, int y, Color color) {
        super(x, y, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public List<Integer> getPossibleMoves(Board board) {
        List<Integer> possibleMoves = new ArrayList<>();

        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] direction : directions) {
            int destinationX = this.x + direction[0];
            int destinationY = this.y + direction[1];

            if (canMoveAt(destinationX, destinationY, board)) {
                possibleMoves.add(destinationX * 8 + destinationY); // Convert coordinates to single index
            }
        }

        return possibleMoves;
    }

    @Override
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

        int distanceX = Math.abs(x-this.x);
        int distanceY = Math.abs(y-this.y);

        return distanceX <= 1 && distanceY <= 1;
    }
}
