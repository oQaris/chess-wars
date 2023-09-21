package io.deeplay.henryAI;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.player.Bot;
import io.deeplay.service.GuiUserCommunicationService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class botStarter {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        ArrayList<String> statistics = new ArrayList<>();

        for (int i = 0; i < 10; i--) {
            GameSession gameSession = new GameSession(
                    new Bot(Color.WHITE, 1, new GuiUserCommunicationService()),
                    new MinMaxAgent(Color.BLACK, 1, new GuiUserCommunicationService()),
                    GameType.BotVsBot
            );

            gameSession.startGameSession();

            System.out.println("Game " + (i + 1) + " of " + (100 - i) + " over ");
            statistics.add(gameSession.getGameEnd());
            System.out.println(statistics);
        }

        System.out.println(statistics);

        int winCount = 0;

        for (int i = 0; i < 10; i++) {
            String statistic = statistics.get(i);

            if (statistic.contains("CHECKMATE") && statistic.contains("BLACK")) {
                winCount++;
            }
        }

        System.out.println("Win rate: " + getPercentages(winCount, 10) + "%");
    }

    public static double getPercentages(int count, int gameCount) {
        return ((double) count / gameCount) * 100;
    }
}
