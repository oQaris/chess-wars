package io.deeplay.model.piece;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(Coordinates coordinates, Color color) {
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
            if (canMoveAt(new Coordinates(coordinates.getX(), i), board)) {
                possibleMoves.add((coordinates.getX() * 8) + i);
            }

            if (canMoveAt(new Coordinates(i, coordinates.getY()), board)) {
                possibleMoves.add((i * 8) + coordinates.getY());
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

        int xDirection = Integer.compare(coordinates.getX(), this.coordinates.getX());
        int yDirection = Integer.compare(coordinates.getY(), this.coordinates.getY());

        int currentX = this.coordinates.getX() + xDirection;
        int currentY = this.coordinates.getY() + yDirection;

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
        return "Rook: x = " + coordinates.getX() + " y = " + coordinates.getY();
    }
}
