package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class Piece {
    private Coordinates coordinates;
    private Color color;

    public Piece(Coordinates coordinates, Color color) {
        this.coordinates = coordinates;
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public abstract List<Coordinates> getPossibleMoves(Board board);

    public abstract boolean canMoveAt(Coordinates coordinates, Board board);
}