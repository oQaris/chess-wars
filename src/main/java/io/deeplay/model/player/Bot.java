package io.deeplay.model.player;

import io.deeplay.model.Board;
import io.deeplay.model.move.Move;

import java.util.List;
import java.util.Random;

public class Bot extends Player {
    private int difficultyLevel;

    public Bot(char piecesColor, int difficultyLevel) {
        super(piecesColor);
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public Board move(Board board) {
        List<Move> allPossibleMoves = board.getAllPossibleMoves();
        Random random = new Random();
        Move randomMove  = allPossibleMoves.get(random.nextInt(allPossibleMoves.size() - 1));
        board.movePiece(randomMove.getStartPosition(), randomMove.getEndPosition());

        return board;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
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
}