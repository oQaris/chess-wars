package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.minimax.ExpectiminimaxAgent;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.service.IUserCommunication;

public class ExpectiminimaxBot extends Bot {
    private static final int DEPTH = 3;
    public ExpectiminimaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
    }

    @Override
    public Move getMove(Board board, Color currentColor) {
        return new ExpectiminimaxAgent().getBestMove(board, DEPTH, getColor());
    }
}
