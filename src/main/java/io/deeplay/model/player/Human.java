
package io.deeplay.model.player;


import io.deeplay.model.Move;
import io.deeplay.model.piece.Color;

import java.util.List;

public class Human extends Player {
    public Human(Color piecesColor) {
        super(piecesColor);
    }

    @Override
    public Move move(List<Move> moves) {
        return null;
    }

}
