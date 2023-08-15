package io.deeplay.model;

import java.util.Objects;

/**
 * The Coordinates class represents a position on a two-dimensional grid.
 */

public class Coordinates {
    private static final int BOARD_LENGTH = 8;
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            throw new IllegalArgumentException("Coordinates must be between 0 and " + (BOARD_LENGTH - 1));
        }

        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        if (x < 0 || x >= 8) {
            throw new IllegalArgumentException("Coordinate must be between 0 and " + (BOARD_LENGTH - 1));
        }

        this.x = x;
    }

    public void setY(int y) {
        if (y < 0 || y >= 8) {
            throw new IllegalArgumentException("Coordinate must be between 0 and " + (BOARD_LENGTH - 1));
        }

        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates that)) return false;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(x = " + x + ", y = " + y + ")";
    }
}