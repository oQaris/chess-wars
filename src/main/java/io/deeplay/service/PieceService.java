package io.deeplay.service;

import io.deeplay.model.piece.Color;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.piece.Rook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PieceService {

    public static List<Piece> getPossibleToMovePieces() {
        // вернуть List с фигурами

        return new ArrayList<>(List.of(new Rook(0, 0, Color.WHITE)));
    }
}
