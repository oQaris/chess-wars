package io.deeplay.service;

import io.deeplay.model.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PieceService {

    public static List<Piece> getPossibleToMovePieces() {
        // вернуть List с фигурами

        return new ArrayList<>(List.of(new Piece()));
    }
}
