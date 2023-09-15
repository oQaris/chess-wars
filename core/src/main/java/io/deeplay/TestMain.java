package io.deeplay;

import io.deeplay.igorAI.MinimaxBot;
import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.player.Bot;
import io.deeplay.service.GuiUserCommunicationService;

import java.nio.charset.StandardCharsets;

public class TestMain {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));

        long begin = System.currentTimeMillis();
        long games = 1500;

        System.out.println("MinimaxBot vs Random");
        for (int i = 0; i < games; i++) {
            GameSession gameSession = new GameSession(
                    new MinimaxBot(Color.WHITE, 1, new GuiUserCommunicationService()),
                    new Bot(Color.BLACK, 1, new GuiUserCommunicationService()),
                    GameType.BotVsBot);
            gameSession.startGameSession();
        }
//        RankingSystem rankingSystem = new RankingSystem();
//
//        for (int i = 0; i < games; i++) {
//            List<CompetitivePlayer> competitivePlayersList = rankingSystem.getTable();
//            System.out.println("\nGAME №" + i);
//
//            Collections.shuffle(competitivePlayersList);
//
////            Random random = new Random();
////            Color randomColor = Color.values()[random.nextInt(Color.values().length - 1)];
//
//            CompetitivePlayer competitivePlayer1 = competitivePlayersList.get(0);
////            CompetitivePlayer competitivePlayer1 = new CompetitivePlayer(new Bot(randomColor, 1,
////                new GuiUserCommunicationService()), BotType.RANDOM, 1000, 0, 0, 0);
//            CompetitivePlayer competitivePlayer2 = competitivePlayersList.get(1);
//
//            Color oppositeColor = competitivePlayer1.getBot().getColor().opposite();
//            Bot tempBot;
//
//            switch (competitivePlayer2.getBotType()) {
//                case EXPECTIMINIMAX ->
//                        tempBot = new ExpectimaxBot(oppositeColor, 1, new GuiUserCommunicationService());
//                case MINIMAX -> tempBot = new MinimaxBot(oppositeColor, 1, new GuiUserCommunicationService());
//                case NEGAMAX -> tempBot = new NegamaxBot(oppositeColor, 1, new GuiUserCommunicationService());
//                default -> tempBot = new Bot(oppositeColor, 1, new GuiUserCommunicationService());
//            }
//
//            competitivePlayer2.setBot(tempBot);
//
//            System.out.println("Player 1: " + competitivePlayer1.getBotType()
//                    + ", color=" + competitivePlayer1.getBot().getColor());
//            System.out.println("Player 2: " + competitivePlayer2.getBotType()
//                    + ", color=" + competitivePlayer2.getBot().getColor());
//
//
//            GameSession gameSession = new GameSession(
//                    competitivePlayer1.getBot(),
//                    competitivePlayer2.getBot(),
//                    GameType.BotVsBot);
//            gameSession.startGameSession();
//
//            List<String> gameEnd = gameSession.getGameEnd();
//
//            GameStates gameEndingType;
//            Color winner = null;
//
//            switch (gameEnd.get(0)) {
//                case "CHECKMATE" -> gameEndingType = GameStates.CHECKMATE;
//                case "STALEMATE" -> gameEndingType = GameStates.STALEMATE;
//                case "DRAW" -> gameEndingType = GameStates.DRAW;
//                default -> gameEndingType = null;
//            }
//
//            if (gameEnd.size() > 1) {
//                switch (gameEnd.get(1)) {
//                    case "WHITE" -> winner = Color.WHITE;
//                    case "BLACK" -> winner = Color.BLACK;
//                }
//            }
//
//            rankingSystem.processGameResult(competitivePlayer1, competitivePlayer2, gameEndingType, winner);
//        }
        long end = System.currentTimeMillis();

//        rankingSystem.getRankingTable();

        System.out.println("\n\nСреднее время 1 матча = " + (end-begin) / games);
    }
}
