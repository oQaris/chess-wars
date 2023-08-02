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
        if (coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getX() >= 8 || coordinates.getY() >= 8) {
            return false;
        }
        if (this.getCoordinates().getX() == coordinates.getX() && this.getCoordinates().getY() == coordinates.getY()){
            return false;
        }

        int xDirection = Integer.compare(coordinates.getX(), this.getCoordinates().getX());
        int yDirection = Integer.compare(coordinates.getY(), this.getCoordinates().getY());

        int distanceX = Math.abs(coordinates.getX() - this.getCoordinates().getX());
        int distanceY = Math.abs(coordinates.getY() - this.getCoordinates().getY());

        // добавляем условие для атаки
        if (distanceX == 1 && distanceY == 1 && board.getPiece(coordinates) != null && board.getPiece(coordinates).getColor() != this.getColor()) {
            return true;
        }
        if (distanceX > 1 || distanceY > 2) {
            return false;
        }

        if (distanceX == 0 && distanceY <= 1 && board.getBoard()[coordinates.getX()][coordinates.getY()].getColor() == Color.EMPTY) {
            return true;
        }

        if (distanceX == 1 && distanceY == 1 && board.getBoard()[coordinates.getX()][coordinates.getY()].getColor() == getColor().opposite()) {
            return true;
        }

        if (distanceX == 0 && distanceY == 2 && ((this.getCoordinates().getY() == 1 && getColor() == Color.WHITE) || (this.getCoordinates().getY() == 6 && getColor() == Color.BLACK))) {
            if (board.getBoard()[this.getCoordinates().getX()][this.getCoordinates().getY() + yDirection].getColor() != Color.EMPTY) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Pawn: x = " + getCoordinates().getX() + ", y = " + getCoordinates().getY();
    }
}
