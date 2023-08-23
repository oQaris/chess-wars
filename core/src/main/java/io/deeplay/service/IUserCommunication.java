package io.deeplay.service;

import io.deeplay.domain.Color;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Piece;

import java.util.List;

public interface IUserCommunication {
    GameSession getGameSessionInfo();

    Piece selectPiece(List<Piece> possiblePiecesToMove);

    int chooseBotLevel();

    Color[] chooseColor();

    boolean continueToPlay();

    Coordinates selectCoordinates(List<Coordinates> availableMoves);

    SwitchPieceType selectSwitchPiece();
}
