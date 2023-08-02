package io.deeplay.domain;

public enum Color {
    WHITE,
    BLACK,
    EMPTY;

    public Color opposite() {
        if (this == WHITE) {
            return BLACK;
        } else if (this == BLACK) {
            return WHITE;
        }
        return EMPTY;
    }
}
