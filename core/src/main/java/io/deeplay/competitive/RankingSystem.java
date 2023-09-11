package io.deeplay.competitive;

import io.deeplay.domain.BotType;
import io.deeplay.domain.Color;
import io.deeplay.domain.GameStates;
import io.deeplay.model.CompetitivePlayer;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.ExpectiminimaxBot;
import io.deeplay.model.player.MinimaxBot;
import io.deeplay.model.player.NegamaxBot;
import io.deeplay.service.GuiUserCommunicationService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingSystem {
    @Getter
    private final List<CompetitivePlayer> table;

    /**
     * Инициализирует 4 разных ботов
     */
    public RankingSystem() {
        table = new ArrayList<>();
        table.add(new CompetitivePlayer(new Bot(Color.WHITE, 1,
                new GuiUserCommunicationService()), BotType.RANDOM, 1000, 0, 0, 0));
        table.add(new CompetitivePlayer(new MinimaxBot(Color.WHITE, 1,
                new GuiUserCommunicationService()), BotType.MINIMAX, 1000, 0, 0, 0));
        table.add(new CompetitivePlayer(new NegamaxBot(Color.WHITE, 1,
                new GuiUserCommunicationService()), BotType.NEGAMAX, 1000, 0, 0, 0));
        table.add(new CompetitivePlayer(new ExpectiminimaxBot(Color.WHITE, 1,
                new GuiUserCommunicationService()), BotType.EXPECTIMINIMAX, 1000, 0, 0, 0));
    }

    /**
     * Метод получает информацию о конце игры, обрабатывает ее и сохраняет
     *
     * @param competitivePlayer1 игрок 1
     * @param competitivePlayer2 игрок 2
     * @param gameStates тип конца игры
     * @param winnerColor цвет победителя
     */
    public void processGameResult(CompetitivePlayer competitivePlayer1, CompetitivePlayer competitivePlayer2,
                                         GameStates gameStates, Color winnerColor) {
        if (gameStates == GameStates.DRAW || gameStates == GameStates.STALEMATE) {
            competitivePlayer1.updateEloRating(competitivePlayer2, gameStates, false);
            competitivePlayer2.updateEloRating(competitivePlayer1, gameStates, false);
            updateTable(competitivePlayer1, competitivePlayer2);
        } else {
            CompetitivePlayer winner = getCompetitivePlayer(competitivePlayer1, competitivePlayer2, winnerColor);
            CompetitivePlayer loser = getCompetitivePlayer(competitivePlayer1, competitivePlayer2, winnerColor.opposite());

            winner.updateEloRating(loser, gameStates, true);
            loser.updateEloRating(winner, gameStates, false);
            updateTable(winner, loser);
        }
    }

    /**
     * Метод нужен для вывода в консоль текущего состояния таблицы рейтинга
     */
    public void getRankingTable() {
        table.sort(Comparator.comparingInt(CompetitivePlayer::getWins).reversed());
        System.out.println();

        for (CompetitivePlayer competitivePlayer : table) {
            System.out.println(competitivePlayer.getBotType()
                    + ": score=" + competitivePlayer.getScore()
                    + ", wins=" + competitivePlayer.getWins()
                    + ", loses=" + competitivePlayer.getLoses()
                    + ", draws=" + competitivePlayer.getDraws());
        }
    }

    /**
     * Добавляет игрока в рейтинговую таблицу
     * @param competitivePlayer игрок, которого нужно добавить
     */
    void addUserToTable(CompetitivePlayer competitivePlayer) {
        table.add(competitivePlayer);
    }

    /**
     * Получает на вход игроков и цвет. Нужен для определения игрока, какой из объектов имеет переданный цвет.
     * @param competitivePlayer1 игрок 1
     * @param competitivePlayer2 игрок 2
     * @param color цвет
     * @return возвращает игрока по цвету
     */
    CompetitivePlayer getCompetitivePlayer(CompetitivePlayer competitivePlayer1, CompetitivePlayer competitivePlayer2,
                                                   Color color) {
        return competitivePlayer1.getBot().getColor() == color ? competitivePlayer1 : competitivePlayer2;
    }

    /**
     * Метод обновляет рейтинговую таблицу.
     * @param winner победивший игрок
     * @param loser проигравший игрок
     */
    void updateTable(CompetitivePlayer winner, CompetitivePlayer loser) {
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).getBotType() == winner.getBotType()) {
                table.set(i, winner);
            } else if (table.get(i).getBotType() == loser.getBotType()) {
                table.set(i, loser);
            }
        }
    }
}