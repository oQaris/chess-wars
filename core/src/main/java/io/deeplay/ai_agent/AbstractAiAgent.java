package io.deeplay.ai_agent;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static io.deeplay.model.player.Player.getPiecesPossibleToMove;
import static io.deeplay.model.player.Player.getType;

public abstract class AbstractAiAgent {
    public Move getRandomMove(List<Move> allPossibleMoves) {
        return allPossibleMoves.get(new Random().nextInt(allPossibleMoves.size()));
    }

    public List<Move> getAllPossibleMoves(Board board, Color color) {
        List<Piece> possiblePiecesToMove = getPiecesPossibleToMove(board, color);
        List<Move> allPossibleMoves = new ArrayList<>();

        for (Piece piece : possiblePiecesToMove) {
            List<Coordinates> movesCoordinatesWithoutCheck = GameState.getMovesWithoutMakingCheck(board, piece, piece.getPossibleMoves(board));

            for (Coordinates coordinates : movesCoordinatesWithoutCheck) {
                MoveType moveType = Player.getType(piece, coordinates, board);
                if (moveType == MoveType.PROMOTION) {
                    for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                        allPossibleMoves.add(new Move(piece.getCoordinates(), coordinates, moveType, SwitchPieceType.values()[i]));
                    }
                } else {
                    allPossibleMoves.add(new Move(piece.getCoordinates(), coordinates, moveType, SwitchPieceType.NULL));
                }
            }
        }

        return allPossibleMoves;
    }
}
