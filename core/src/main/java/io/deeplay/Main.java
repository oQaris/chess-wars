package io.deeplay;

import io.deeplay.engine.GameSession;
import io.deeplay.service.UserCommunicationService;

import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        GameSession gameSession = UserCommunicationService.getGameSessionInfo();
        gameSession.startGameSession();
    }
}