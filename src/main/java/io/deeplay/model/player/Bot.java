package io.deeplay.model.player;

public class Bot extends Player {
    private int difficultyLevel;

    public Bot(char piecesColor, int difficultyLevel) {
        super(piecesColor);
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public void move() {
        // random move
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