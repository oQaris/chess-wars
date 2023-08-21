package io.deeplay.server;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;

public class ServerPlayer extends Player {
    public ServerPlayer(Color color) {
        super(color);
    }

    @Override
    public Move getMove(Board board, Color currentColor) {
        return new Move(new Coordinates(1, 1),
                new Coordinates(1, 2), MoveType.ORDINARY, SwitchPieceType.NULL);
    }
}
