package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    /**
     * Метод для получения возможных ходов фигуры Queen (Ферзь) на доске.
     *
     * @param board доска, на которой находится фигура
     * @return список возможных ходов фигуры
     */
    @Override
    public List<Coordinates> getPossibleMoves(Board board) {
        Rook rook = new Rook(getCoordinates(), getColor()); // Add possible moves for Rook
        List<Coordinates> possibleMoves = new ArrayList<>(rook.getPossibleMoves(board));

        // Add possible moves for Bishop
        Bishop bishop = new Bishop(getCoordinates(), getColor());
        possibleMoves.addAll(bishop.getPossibleMoves(board));

        return possibleMoves;
    }

    /**
     * Проверяет, может ли данная фигура Queen (ферзь) сделать ход на заданные координаты на заданной доске.
     * @param coordinates координаты для проверки возможности хода
     * @param board доска для проверки возможности хода
     * @return true, если ход возможен, false в противном случае
     */
    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        int dx = Math.abs(coordinates.getX() - getCoordinates().getX());
        int dy = Math.abs(coordinates.getY() - getCoordinates().getY());

        if (dx == 0 || dy == 0) {
            // Moving horizontally or vertically like a Rook
            Rook rook = new Rook(getCoordinates(), getColor());
            return rook.canMoveAt(coordinates, board);
        } else if (dx == dy) {
            // Moving diagonally like a Bishop
            Bishop bishop = new Bishop(getCoordinates(), getColor());
            return bishop.canMoveAt(coordinates, board);
        }

        return false;
    }
    @Override
    public String toString() {
        return "Queen: x = " + getCoordinates().getX() + ", y = " + getCoordinates().getY();
    }
}
