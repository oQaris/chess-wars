package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Empty extends Piece {
    public Empty(Coordinates coordinates) {
        super(coordinates, Color.EMPTY);
    }

    /** У пустой клетки нет возможных ходов, возвращается пустой лист **/
    @Override
    public List<Coordinates> getPossibleMoves(Board board) {
        return new ArrayList<>();
    }

    /** Пустая клетка не может никуда походить, возвращается false **/
    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        return false;
    }

    @Override
    public String toString() {
        return "Empty: x = " + getCoordinates().getX() + " y = " + getCoordinates().getY();
    }
}