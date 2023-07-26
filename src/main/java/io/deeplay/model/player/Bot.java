package io.deeplay.model.player;





import io.deeplay.model.piece.Color;

import java.util.Random;

public class Bot extends Player {
    private int difficultyLevel;
    private Color piecesColor;

    public Bot(Color piecesColor, int difficultyLevel) {
        super(piecesColor);
        this.difficultyLevel = difficultyLevel;
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
                ", piecesColor=" + piecesColor +
                '}';
    }

    @Override
    public void move() {
        Random random = new Random();
        return allPossibleMoves.get(random.nextInt(allPossibleMoves.size() - 1));
    }
}