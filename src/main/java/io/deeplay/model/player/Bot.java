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
        Move randomMove = getRandomMove(allPossibleMoves);
        board.movePiece(randomMove.getStartPosition(), randomMove.getEndPosition());

        return board;
    }

    private Move getRandomMove(List<Move> allPossibleMoves) {
        Random random = new Random();
        return allPossibleMoves.get(random.nextInt(allPossibleMoves.size() - 1));
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