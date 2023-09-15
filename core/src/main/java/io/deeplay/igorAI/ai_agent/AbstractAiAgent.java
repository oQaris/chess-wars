package io.deeplay.igorAI.ai_agent;

import io.deeplay.domain.Color;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.deeplay.model.player.Player.getPiecesPossibleToMove;

public abstract class AbstractAiAgent {
    public Move getRandomMove(List<Move> allPossibleMoves) {
        return allPossibleMoves.get(new Random().nextInt(allPossibleMoves.size()));
    }

    public List<Move> getAllPossibleMoves(Board board, Color color) {
        List<Piece> possiblePiecesToMove = getPiecesPossibleToMove(board, color);
        List<Move> allPossibleMoves = new ArrayList<>();

        for (Piece piece : possiblePiecesToMove) {
            allPossibleMoves.addAll(GameState.getMovesListWithoutMakingCheck(board, piece, piece.getPossibleMoves(board)));
        }

        return allPossibleMoves;
    }
}
