package io.deeplay.domain;

import java.util.Random;

public enum SwitchPieceType {
    BISHOP,
    KNIGHT,
    QUEEN,
    ROOK,
    NULL;

    public static SwitchPieceType getRandomPiece() {
        Random random = new Random();
        return values()[random.nextInt(values().length - 1)];
    }
}