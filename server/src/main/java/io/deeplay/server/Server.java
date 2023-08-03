package io.deeplay.server;

import io.deeplay.engine.GameSession;
import io.deeplay.service.UserCommunicationService;

public class Server {
    public static void main(String[] args) {
        GameSession gameSession = UserCommunicationService.getGameSessionInfo();
        gameSession.startGameSession();
    }
}