package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.engine.GameInfo;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.service.MoveService;

import java.util.List;
import java.util.Random;

public class Bot extends Player {
    private int difficultyLevel;
    private Color color;

    public Bot(Color color, int difficultyLevel) {
        super(color);
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public Move getMove(List<Piece> possiblePiecesToMove, Board board) {
        Random random = new Random();

        Piece randomPiece = null;
        if (possiblePiecesToMove.size() == 1) randomPiece = possiblePiecesToMove.get(0);
        else randomPiece = possiblePiecesToMove.get(random.nextInt(possiblePiecesToMove.size() - 1));

        List<Coordinates> availableMoves = randomPiece.getPossibleMoves(board);

        System.out.println("Number of moves you can do using this Piece: " + availableMoves.size());

        Coordinates randomMoveCoordinates = null;
        if (availableMoves.size() == 1) randomMoveCoordinates = availableMoves.get(0);
        else randomMoveCoordinates = availableMoves.get(random.nextInt(availableMoves.size() - 1));

        System.out.println("Bot selected to move: " + randomPiece.getColor() + " " + randomPiece.getClass().getSimpleName()
                + " to coordinates: x=" + randomMoveCoordinates.getX() + ", y=" + randomMoveCoordinates.getY());
        return MoveService.createMove(randomPiece, randomMoveCoordinates, board);
    }

    public void chooseDifficultyLevel(int level) {
        switch (level) {
            case 1: // easy
                break;
            case 2: //medium
                break;
            case 3: //hard
                break;
            default: // incorrect input
                break;
        }
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    @Override
    public String toString() {
        return "Bot{" +
                "difficultyLevel=" + difficultyLevel +
                ", color=" + color +
                '}';
    }
}