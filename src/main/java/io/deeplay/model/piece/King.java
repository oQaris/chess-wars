package io.deeplay.model.piece;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public List<Integer> getPossibleMoves(Board board) {
        List<Integer> possibleMoves = new ArrayList<>();

        // проверяем все клетки вокруг короля
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = coordinates.getX() + i;
                int y = coordinates.getY() + j;

                possibleMoves.add(x * 8 + y);
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        if (coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getY() >= 8 || coordinates.getX() >= 8) {
            return false;
        }

        if (this.coordinates.getX() == coordinates.getX() && this.coordinates.getY() == coordinates.getY()) {
            return false;
        }

        if (board.getBoard()[coordinates.getX()][coordinates.getY()].getColor().equals(color)) { // фигура того же цвета
            return false;
        }

        int distanceX = Math.abs(coordinates.getX() - this.coordinates.getX());
        int distanceY = Math.abs(coordinates.getY() - this.coordinates.getY());

        return distanceX <= 1 && distanceY <= 1;
    }

    @Override
    public String toString() {
        return "King: x = " + coordinates.getX() + " y = " + coordinates.getY();
    }
}
