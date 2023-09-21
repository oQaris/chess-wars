package gui;

import io.deeplay.model.move.Move;

import java.util.List;

public interface EndpointUser {
    void initialize();

    void endGame(List<String> endGameInfo);

    void waitAndUpdate();

    void updateGameInfo(Move incomingMove);
}