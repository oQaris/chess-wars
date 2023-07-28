package io.deeplay.engine;

import io.deeplay.service.UserCommunicationService;

public class GameSession {

    private final SelfPlay selfPlay;

    public GameSession() {
        selfPlay = UserCommunicationService.getGameInfo();
    }

    public void startGameSession() {
        selfPlay.chooseMove();
    }
}
