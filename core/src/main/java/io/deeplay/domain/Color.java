package io.deeplay.domain;

public enum Color {
    WHITE,
    BLACK,
    EMPTY;

    /**
     * @return противоположный цвет
     */
    public Color opposite() {
        if (this == WHITE) {
            return BLACK;
        } else if (this == BLACK) {
            return WHITE;
        }

        throw new IllegalArgumentException("Can't define Color");
    }
}
