package io.deeplay.model.piece;

import io.deeplay.domain.Color;
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
    public List<Coordinates> getPossibleMoves(Board board) {
        List<Coordinates> possibleMoves = new ArrayList<>();
        int x = getCoordinates().getX();
        int y = getCoordinates().getY();

        for (int i = 0; i < 8; i++) {
            if (canMoveAt(new Coordinates(x, i), board)) {
                possibleMoves.add(new Coordinates(x, i));
            }

            if (canMoveAt(new Coordinates(i, y), board)) {
                possibleMoves.add(new Coordinates(i, y));
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        if (coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getY() >= 8 || coordinates.getX() >= 8) {
            return false;
        }

        if (this.getCoordinates().getX() == coordinates.getX() && this.getCoordinates().getY() == coordinates.getY()) {
            return false;
        }

        if (board.getBoard()[coordinates.getX()][coordinates.getY()].getColor().equals(getColor())) { // фигура того же цвета
            return false;
        }

        if (Math.abs(coordinates.getX() - this.getCoordinates().getX()) == Math.abs(coordinates.getY() - this.getCoordinates().getY())) {
            return false;
        }

        int xDirection = Integer.compare(coordinates.getX(), this.getCoordinates().getX());
        int yDirection = Integer.compare(coordinates.getY(), this.getCoordinates().getY());

        int currentX = this.getCoordinates().getX() + xDirection;
        int currentY = this.getCoordinates().getY() + yDirection;

        while (currentX != coordinates.getX() || currentY != coordinates.getY()) {
            if(currentX < 0 || currentY < 0 || currentX >= 8 || currentY >= 8) return false;
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
        return "Rook: x = " + getCoordinates().getX() + " y = " + getCoordinates().getY();
    }
}
