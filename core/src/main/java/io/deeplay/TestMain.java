package io.deeplay;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.MinimaxBot;
import io.deeplay.model.player.NegamaxBot;
import io.deeplay.service.GuiUserCommunicationService;

import java.nio.charset.StandardCharsets;

public class TestMain {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));

        long begin = System.currentTimeMillis();
        long games = 50;

        for (int i = 0; i < games; i++) {
            GameSession gameSession = new GameSession(
                    new Bot(Color.WHITE, 1, new GuiUserCommunicationService()),
                    new NegamaxBot(Color.BLACK, 1, new GuiUserCommunicationService()),
                    GameType.BotVsBot);
            gameSession.startGameSession();
        }
        long end = System.currentTimeMillis();

        System.out.println("\n\nСреднее время 1 матча = " + (end-begin) / games);
    }
}
