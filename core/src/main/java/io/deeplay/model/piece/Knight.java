package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    /**
     * Метод для получения возможных ходов фигуры Knight (конь) на доске.
     *
     * @param board доска, на которой находится фигура
     * @return список возможных ходов фигуры
     */
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

    /**
     * Проверяет, может ли данная фигура Knight (конь) сделать ход на заданные координаты на заданной доске.
     * @param coordinates координаты для проверки возможности хода
     * @param board доска для проверки возможности хода
     * @return true, если ход возможен, false в противном случае
     */
    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        if (coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getY() >= 8 || coordinates.getX() >= 8) {
            return false;
        }

        if (board.getBoard()[coordinates.getX()][coordinates.getY()].getColor().equals(getColor())) {
            return false;
        }

        int xDifference = Math.abs(coordinates.getX() - this.getCoordinates().getX());
        int yDifference = Math.abs(coordinates.getY() - this.getCoordinates().getY());

        return (xDifference == 1 && yDifference == 2) || (xDifference == 2 && yDifference == 1);
    }

    @Override
    public String toString() {
        return "Knight: x = " + getCoordinates().getX() + ", y = " + getCoordinates().getY();
    }
}