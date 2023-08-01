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

        int[] dx = {-1, 1, -1, 1};
        int[] dy = {-1, -1, 1, 1};

        for (int i = 0; i < dx.length; i++) {
            int currentX = getCoordinates().getX() + dx[i];
            int currentY = getCoordinates().getY() + dy[i];

            while (currentX >= 0 && currentX < 8 && currentY >= 0 && currentY < 8) {
                Coordinates coordinates = new Coordinates(currentX, currentY);
                if (canMoveAt(coordinates, board)) {
                    possibleMoves.add(coordinates);
                }
                currentX += dx[i];
                currentY += dy[i];
            }
        }
        return possibleMoves;
    }

    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {

        int dx = coordinates.getX() - getCoordinates().getX();
        int dy = coordinates.getY() - getCoordinates().getY();

        if (Math.abs(dx) != Math.abs(dy)) {
            return false;
        }

        int xDirection = Integer.compare(coordinates.getX(), getCoordinates().getX());
        int yDirection = Integer.compare(coordinates.getY(), getCoordinates().getY());

        int currentX = getCoordinates().getX() + xDirection;
        int currentY = getCoordinates().getY() + yDirection;

        while (currentX != coordinates.getX() && currentY != coordinates.getY()) {
            if (board.getPiece(new Coordinates(currentX, currentY)).equals(Color.EMPTY)) {
                return false;
            }

            currentX += xDirection;
            currentY += yDirection;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Pawn: x = " + getCoordinates().getX() + ", y = " + getCoordinates().getY();
    }
}
