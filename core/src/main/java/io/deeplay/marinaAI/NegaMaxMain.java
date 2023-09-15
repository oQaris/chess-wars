package io.deeplay.marinaAI;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.engine.GameSession;
import io.deeplay.marinaAI.bot.NegaMaxBot;
import io.deeplay.model.player.Bot;
import io.deeplay.service.GuiUserCommunicationService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NegaMaxMain {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));
        ArrayList<String> statistics = new ArrayList<>();

        int gameCount = 1000;

        for (int i = 0; i < gameCount; i++) {
            GameSession gameSession = new GameSession(
                    new Bot(Color.WHITE, 1, new GuiUserCommunicationService()),
                    new NegaMaxBot(Color.BLACK, 1, new GuiUserCommunicationService()),
                    GameType.BotVsBot
            );

            gameSession.startGameSession();

            System.out.println("Game " + (i + 1) + " of " + gameCount + " over ");
            statistics.add(gameSession.getGameEnd());
            System.out.println(statistics);
        }

        System.out.println(statistics);

        int winCount = 0;
        int loseCount = 0;
        int drawCount = 0;
        int staleMateCount = 0;

        for (int i = 0; i < gameCount; i++) {
            String statistic = statistics.get(i);

            if (statistic.contains("CHECKMATE") && statistic.contains("BLACK")) {
                winCount++;
            } else if (statistic.contains("STALEMATE")) {
                staleMateCount++;
            } else if (statistic.contains("DRAW")) {
                drawCount++;
            } else if (statistic.contains("WHITE") && statistic.contains("CHECKMATE")) {
                loseCount++;
            }
        }

        System.out.println("Win rate: " + getPercentages(winCount, gameCount) + "%");
        System.out.println("Lose rate: " + getPercentages(loseCount, gameCount) + "%");
        System.out.println("Draw rate: " + getPercentages(drawCount, gameCount) + "%");
        System.out.println("StaleMate rate: " + getPercentages(staleMateCount, gameCount) + "%");
    }

    public static double getPercentages(int count, int gameCount) {
        return ((double) count / gameCount) * 100;
    }
}
