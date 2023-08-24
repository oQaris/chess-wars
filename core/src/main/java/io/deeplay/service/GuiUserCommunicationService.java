package io.deeplay.service;

import io.deeplay.domain.Color;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Piece;

import java.util.List;

public class GuiUserCommunicationService implements IUserCommunication {

    @Override
    public GameSession getGameSessionInfo() {
        throw new IllegalStateException();
    }

    @Override
    public Piece selectPiece(List<Piece> possiblePiecesToMove) {
        throw new IllegalStateException();
    }

    @Override
    public int chooseBotLevel() {
        return 0;
    }

    @Override
    public Color[] chooseColor() {
        return new Color[0];
    }

    @Override
    public boolean continueToPlay() {
        return false;
    }

    @Override
    public Coordinates selectCoordinates(List<Coordinates> availableMoves) {
        throw new IllegalStateException();
    }

    @Override
    public SwitchPieceType selectSwitchPiece() {
        throw new IllegalStateException();
    }
}
