package io.deeplay.marinaAI.bot;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.marinaAI.strategy.MaterialStrategy;
import io.deeplay.marinaAI.strategy.Strategy;
import io.deeplay.marinaAI.utils.ScoredMove;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Bot;
import io.deeplay.service.BoardUtil;
import io.deeplay.service.IUserCommunication;

import java.util.ArrayList;
import java.util.List;

public class ExpectiMaxBot extends Bot {
    private static final int MAX_DEPTH = 3;
    private final Color maximizingColor;
    private Strategy strategy;

    public ExpectiMaxBot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
        maximizingColor = color;
        // strategy = new PestoStrategy(maximizingColor);
        strategy = new MaterialStrategy(maximizingColor);
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Move getMove(Board board, Color currentColor) {
        ScoredMove bestMove = expectiMax(board, MAX_DEPTH, currentColor);
        return bestMove.getMove();
    }

    private ScoredMove expectiMax(Board board, int depth, Color currentColor) {
        if (depth == 0 || GameState.isGameOver(board, currentColor)) {
            int score = strategy.evaluate(board);

            return new ScoredMove(score, null);
        }

        List<Piece> possiblePieces = getPiecesPossibleToMove(board, currentColor);
        List<ScoredMove> scoredMoves = new ArrayList<>();

        for (Piece piece : possiblePieces) {
            List<Coordinates> possibleMoves = piece.getPossibleMoves(board);
            List<Coordinates> movesWithoutCheck = GameState.getMovesWithoutMakingCheck(board, piece, possibleMoves);
            possibleMoves.retainAll(movesWithoutCheck);

            for (Coordinates move : possibleMoves) {
                Board duplicateBoard = duplicateBoard(board);
                MoveType moveType = getType(piece, move, duplicateBoard);
                SwitchPieceType switchPieceType = SwitchPieceType.NULL;

                if (moveType == MoveType.PROMOTION) {
                    for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                        Board promotionBoard = duplicateBoard(duplicateBoard);
                        switchPieceType = SwitchPieceType.values()[i];
                        Move currentMove = new Move(piece.getCoordinates(), move, moveType, switchPieceType);
                        promotionBoard.move(currentMove);

                        ScoredMove result = expectiMax(promotionBoard, depth - 1, currentColor.opposite());
                        scoredMoves.add(new ScoredMove(result.getScore(), currentMove));
                    }
                } else {
                    Move currentMove = new Move(piece.getCoordinates(), move, moveType, switchPieceType);
                    duplicateBoard.move(currentMove);

                    ScoredMove result = expectiMax(duplicateBoard, depth - 1, currentColor.opposite());
                    scoredMoves.add(new ScoredMove(result.getScore(), currentMove));
                }
            }
        }

        double totalScore = 0;

        for (ScoredMove scoredMove : scoredMoves) {
            totalScore += scoredMove.getScore();
        }

        double averageScore = totalScore / scoredMoves.size();

        ScoredMove bestScoredMove = scoredMoves.get(0);

        for (ScoredMove scoredMove : scoredMoves) {
            if (scoredMove.getScore() > bestScoredMove.getScore()) {
                bestScoredMove = scoredMove;
            }
        }

        return new ScoredMove((int) averageScore, bestScoredMove.getMove());
    }

    private Board duplicateBoard(Board board) {
        Board duplicateBoard = new Board();
        BoardUtil.duplicateBoard(board).accept(duplicateBoard);

        return duplicateBoard;
    }
}
