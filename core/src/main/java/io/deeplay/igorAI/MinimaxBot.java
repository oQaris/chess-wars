package io.deeplay.igorAI;

import io.deeplay.domain.Color;
import io.deeplay.igorAI.ai_agent.MinimaxAgent;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import io.deeplay.service.IUserCommunication;

public class MinimaxBot extends Bot {
    private static final int DEPTH = 3;
    public MinimaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
    }

    @Override
    public Move getMove(Board board, Color currentColor) {
        return new MinimaxAgent().getBestMove(board, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, getColor());
    }
}
