package io.deeplay.domain;

import java.util.Random;

public enum SwitchPieceType {
    BISHOP,
    KNIGHT,
    QUEEN,
    ROOK,
    NULL;

    /**
     * Метод возвращает рандомную фигуру. Используется для рандомного выбора фигуры для рокировки в боте
     *
     * @return рандомную фигуру
     */
    public static SwitchPieceType getRandomPiece() {
        Random random = new Random();
        return values()[random.nextInt(values().length - 1)];
    }
}