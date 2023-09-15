package io.deeplay.igorAI;

import io.deeplay.igorAI.ai_agent.ExpectimaxAgent;
import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import io.deeplay.service.IUserCommunication;

public class ExpectimaxBot extends Bot {
    private static final int DEPTH = 3;
    public ExpectimaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
    }

    @Override
    public Move getMove(Board board, Color currentColor) {
        return new ExpectimaxAgent().getBestMove(board, DEPTH, getColor());
    }
}
