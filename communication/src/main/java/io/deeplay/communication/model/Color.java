package io.deeplay.communication.model;

public enum Color {
    BLACK,
    WHITE,
    EMPTY;

    /**
     * @return противоположный цвет
     */
    public Color opposite() {
        if (this == WHITE) {
            return BLACK;
        } else {
            return WHITE;
        }
    }
}