package io.deeplay;

import io.deeplay.engine.GameSession;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.utils.BoardUtils;
import io.deeplay.service.UserCommunicationService;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));

        UserCommunicationService userCommunicationService = new UserCommunicationService(System.in, System.out);
        GameSession gameSession = userCommunicationService.getGameSessionInfo();
        gameSession.startGameSession();
    }
}