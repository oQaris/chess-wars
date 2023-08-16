package io.deeplay.communication.model;

public enum Color {
    BLACK,
    WHITE;

    public Color opposite() {
        if (this == WHITE) {
            return BLACK;
        } else {
            return WHITE;
        }
    }
}