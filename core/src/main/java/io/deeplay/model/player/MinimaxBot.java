package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.minimax.MinimaxAgent;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.service.IUserCommunication;

public class MinimaxBot extends Bot {
    public MinimaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
    }

    @Override
    public Move getMove(Board board, Color currentColor) {
        return new MinimaxAgent().getBestMove(board, 3, Double.MIN_VALUE, Double.MAX_VALUE, getColor());
    }
}
