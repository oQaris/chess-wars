package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{
    public Pawn(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    @Override
    public List<Coordinates> getPossibleMoves(Board board) {
        List<Coordinates> possibleMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canMoveAt(new Coordinates(i, j), board)) {
                    possibleMoves.add(new Coordinates(i, j));
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
        if (Math.abs(coordinates.getX() - this.getCoordinates().getX()) == Math.abs(coordinates.getY() - this.getCoordinates().getY())) {
            return false;
        }

        int xDirection = Integer.compare(coordinates.getX(), this.getCoordinates().getX());
        int yDirection = Integer.compare(coordinates.getY(), this.getCoordinates().getY());

        int currentX = this.getCoordinates().getX() + xDirection;
        int currentY = this.getCoordinates().getY() + yDirection;

        int distance = Math.abs(coordinates.getY() - this.getCoordinates().getY());
        if (!board.getBoard()[currentX][currentY].getColor().equals(Color.EMPTY)) { // если не пустая, на пути стоит фигура другого цвета
            return false;
        }
        if ((coordinates.getY() == 1 && board.getPiece(coordinates).getColor() == Color.WHITE) || (coordinates.getY() == 6 && board.getPiece(coordinates).getColor() == Color.BLACK)) {
            if (distance == 2) {
                return true;
            }
        }
        if (this.getCoordinates().getX() == coordinates.getX() && this.getCoordinates().getY() + yDirection == coordinates.getY() && board.getPiece(coordinates).getColor().equals(Color.EMPTY) && distance == 1) {
            return true;
        }
        if (Math.abs(this.getCoordinates().getX() - coordinates.getX()) == 1 && this.getCoordinates().getY() + yDirection == coordinates.getY()) {
            if (!board.getBoard()[coordinates.getX()][coordinates.getY()].getColor().equals(Color.EMPTY)) {
                return true;
            }
        }
        currentX += xDirection;
        currentY += yDirection;
        return false;
    }

    @Override
    public String toString() {
        return "Pawn: x = " + getCoordinates().getX() + ", y = " + getCoordinates().getY();
    }
}
