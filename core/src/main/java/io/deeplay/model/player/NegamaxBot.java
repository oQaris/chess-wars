package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.ai_agent.NegamaxAgent;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.service.IUserCommunication;

public class NegamaxBot extends Bot {
    private static final int DEPTH = 3;
    public NegamaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
    }

    @Override
    public Move getMove(Board board, Color currentColor) {
        return new NegamaxAgent().getBestMove(board, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, getColor());
    }
}
