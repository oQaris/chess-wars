package io.deeplay.model.move;

public class Move {
    private int startPosition;
    private int endPosition;
    private MoveType moveType;
    private char switchTo;

    public Move(int startPosition, int endPosition, MoveType moveType, char switchTo) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.moveType = moveType;
        this.switchTo = switchTo;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public char getSwitchTo() {
        return switchTo;
    }

    public MoveType getMoveType() {
        return moveType;
    }
}