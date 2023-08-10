package io.deeplay.model.move;

import com.google.gson.annotations.SerializedName;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.model.Coordinates;

public class Move {
    @SerializedName("start")
    private Coordinates startPosition;
    @SerializedName("end")
    private Coordinates endPosition;
    @SerializedName("type")
    private MoveType moveType;
    @SerializedName("switchTo")
    private SwitchPieceType switchPieceType;

    public Move(Coordinates startPosition, Coordinates endPosition, MoveType moveType, SwitchPieceType switchPieceType) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.moveType = moveType;
        this.switchPieceType = switchPieceType;
    }

    public Coordinates getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Coordinates startPosition) {
        this.startPosition = startPosition;
    }

    public Coordinates getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Coordinates endPosition) {
        this.endPosition = endPosition;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public SwitchPieceType getSwitchPieceType() {
        return switchPieceType;
    }

    public void setSwitchPieceType(SwitchPieceType switchPieceType) {
        this.switchPieceType = switchPieceType;
    }

    @Override
    public String toString() {
        return "Start position = " + startPosition + " end position + " + endPosition;
    }
}
