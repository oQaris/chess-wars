package gui;

import io.deeplay.model.move.Move;

import java.util.List;

public interface EndpointUser {
    public void initialize();
    public void endGame(List<String> endGameInfo);
    public void waitAndUpdate();
    public void updateGameInfo(Move incomingMove);
}
