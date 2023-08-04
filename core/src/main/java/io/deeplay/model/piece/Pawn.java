package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
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

    public boolean canMoveAt(Coordinates coordinates, Board board) {
        int currentX = getCoordinates().getX();
        int currentY = getCoordinates().getY();
        int targetX = coordinates.getX();
        int targetY = coordinates.getY();

        if (targetX < 0 || targetY < 0 || targetY >= 8 || targetX >= 8) {
            return false;
        }

        if ((currentX == targetX) && (currentY == targetY)) {
            return false;
        }

        if (board.getBoard()[targetX][targetY].getColor().equals(getColor())) { // фигура того же цвета
            return false;
        }

        if (Math.abs(targetY - currentY) > 2 || Math.abs(targetX - currentX) > 1) {
            return false;
        }

        if ((getColor() == Color.WHITE) && targetY <= currentY) { // только вперед хотьба
            return false;
        }

        if ((getColor() == Color.BLACK) && targetY >= currentY) {
            return false;
        }

        if (Math.abs(targetY - currentY) == 2 && isStartPosition() && currentX == targetX) { // только из стартовой позиции
            if (board.getBoard()[currentX][currentY + 1].getColor().equals(Color.EMPTY)
                    || board.getBoard()[currentX][currentY - 1].getColor().equals(Color.EMPTY)) {
                return true;
            }

            return false;
        }

        if (targetX == currentX && Math.abs(targetY - currentY) == 1
                && board.getBoard()[targetX][targetY].getColor().equals(Color.EMPTY)) {
            return true;
        }

        if (Math.abs(targetY - currentY) == 1 && Math.abs(targetX - currentX) == 1) {
            if (!board.getBoard()[targetX][targetY].getColor().equals(Color.EMPTY)) {
                return true;
            }

            return false;
        }

        return false;
    }

    private boolean isStartPosition() {
        return (getColor() == Color.WHITE && getCoordinates().getY() == 1)
                || (getColor() == Color.BLACK && getCoordinates().getY() == 6);
    }

    @Override
    public String toString() {
        return "Pawn: x = " + getCoordinates().getX() + ", y = " + getCoordinates().getY();
    }
}
