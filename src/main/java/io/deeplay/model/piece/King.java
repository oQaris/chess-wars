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
    public List<Coordinates> getPossibleMoves(Board board) {
        List<Coordinates> possibleMoves = new ArrayList<>();

        int x = this.getCoordinates().getX();
        int y = this.getCoordinates().getY();

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && !(i == x && j == y)) {
                    Coordinates coordinates = new Coordinates(i, j);
                    if (canMoveAt(coordinates, board)) {
                        possibleMoves.add(coordinates);
                    }
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

        if (this.getCoordinates().getX() == coordinates.getX() && this.getCoordinates().getY() == coordinates.getY()) {
            return false;
        }

        if (board.getBoard()[coordinates.getX()][coordinates.getY()].getColor().equals(getColor())) { // фигура того же цвета
            return false;
        }

        int distanceX = Math.abs(coordinates.getX() - this.getCoordinates().getX());
        int distanceY = Math.abs(coordinates.getY() - this.getCoordinates().getY());

        return distanceX <= 1 && distanceY <= 1;
    }

    @Override
    public String toString() {
        return "King: x = " + getCoordinates().getX() + " y = " + getCoordinates().getY();
    }
}
