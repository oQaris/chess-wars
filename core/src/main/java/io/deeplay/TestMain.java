package io.deeplay;

import io.deeplay.competitive.RankingSystem;
import io.deeplay.domain.BotType;
import io.deeplay.domain.Color;
import io.deeplay.domain.GameStates;
import io.deeplay.domain.GameType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.CompetitivePlayer;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.ExpectiminimaxBot;
import io.deeplay.model.player.MinimaxBot;
import io.deeplay.model.player.NegamaxBot;
import io.deeplay.service.GuiUserCommunicationService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));

        long begin = System.currentTimeMillis();
        long games = 40;

        RankingSystem rankingSystem = new RankingSystem();

        for (int i = 0; i < games; i++) {
            List<CompetitivePlayer> competitivePlayersList = rankingSystem.getTable();
            System.out.println("\nNEW GAME!");

            Collections.shuffle(competitivePlayersList);

            CompetitivePlayer competitivePlayer1 = competitivePlayersList.get(0);
            CompetitivePlayer competitivePlayer2 = competitivePlayersList.get(1);

            Color oppositeColor = competitivePlayer1.getBot().getColor().opposite();
            Bot tempBot;

            switch (competitivePlayer2.getBotType()) {
                case EXPECTIMINIMAX ->
                        tempBot = new ExpectiminimaxBot(oppositeColor, 1, new GuiUserCommunicationService());
                case MINIMAX -> tempBot = new MinimaxBot(oppositeColor, 1, new GuiUserCommunicationService());
                case NEGAMAX -> tempBot = new NegamaxBot(oppositeColor, 1, new GuiUserCommunicationService());
                default -> tempBot = new Bot(oppositeColor, 1, new GuiUserCommunicationService());
            }

            competitivePlayer2.setBot(tempBot);

            System.out.println("Player 1: " + competitivePlayer1.getBotType()
                    + ", color=" + competitivePlayer1.getBot().getColor());
            System.out.println("Player 2: " + competitivePlayer2.getBotType()
                    + ", color=" + competitivePlayer2.getBot().getColor());


            GameSession gameSession = new GameSession(
                    competitivePlayer1.getBot(),
                    competitivePlayer2.getBot(),
                    GameType.BotVsBot);
            gameSession.startGameSession();

            List<String> gameEnd = gameSession.getGameEnd();

            GameStates gameEndingType;
            Color winner = null;

            switch (gameEnd.get(0)) {
                case "CHECKMATE" -> gameEndingType = GameStates.CHECKMATE;
                case "STALEMATE" -> gameEndingType = GameStates.STALEMATE;
                case "DRAW" -> gameEndingType = GameStates.DRAW;
                default -> gameEndingType = null;
            }

            if (gameEnd.size() > 1) {
                switch (gameEnd.get(1)) {
                    case "WHITE" -> winner = Color.WHITE;
                    case "BLACK" -> winner = Color.BLACK;
                }
            }

            rankingSystem.processGameResult(competitivePlayer1, competitivePlayer2, gameEndingType, winner);
        }
        long end = System.currentTimeMillis();

        rankingSystem.getRankingTable();

        System.out.println("\n\nСреднее время 1 матча = " + (end-begin) / games);
    }
}
