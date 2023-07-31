package io.deeplay.model.piece;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public List<Integer> getPossibleMoves(Board board) {
        List<Integer> possibleMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canMoveAt(new Coordinates(i, j), board)) {
                    possibleMoves.add(i * 8 + j);
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        if (coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getY() >= 8 || coordinates.getX() >= 8) {
            return false;
        }

        if (board.getBoard()[coordinates.getX()][coordinates.getY()].getColor().equals(getColor())) { // фигура того же цвета
            return false;
        }

        if (Math.abs(coordinates.getX() - this.getCoordinates().getX()) != Math.abs(coordinates.getY() - this.getCoordinates().getY())) {
            return false;
        }

        int xDirection = Integer.compare(coordinates.getX(), this.getCoordinates().getX());
        int yDirection = Integer.compare(coordinates.getY(), this.getCoordinates().getY());

        int currentX = this.getCoordinates().getX() + xDirection;
        int currentY = this.getCoordinates().getY() + yDirection;

        while (currentX != coordinates.getX() || currentY != coordinates.getY()) {
            if (!board.getBoard()[currentX][currentY].getColor().equals(Color.EMPTY)) { // если не пустая, на пути стоит фигура другого цвета
                return false;
            }

            currentX += xDirection;
            currentY += yDirection;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Bishop: x = " + getCoordinates().getX() + " y = " + getCoordinates().getY();
    }
}
