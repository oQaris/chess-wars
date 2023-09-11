package io.deeplay.model;

import io.deeplay.domain.BotType;
import io.deeplay.domain.GameStates;
import io.deeplay.model.player.Bot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetitivePlayer {
    private Bot bot;
    private BotType botType;
    private int score;
    private int wins;
    private int loses;
    private int draws;

    /**
     * Метод просчитывает эло текущего игрока. После этого он обновляет score и, в зависимости от конца игры, обновляет
     * поле wins, loses или draws
     * @param opponent противник
     * @param gameStates тип конца игры
     * @param isWin является ли игрок победителем
     */
    public void updateEloRating(CompetitivePlayer opponent, GameStates gameStates, boolean isWin) {
        int K = 32;
        double result = -1;
        switch (gameStates) {
            case CHECKMATE -> {
                if (isWin) {
                    result = 1;
                } else {
                    result = 0;
                }
            }
            case DRAW, STALEMATE -> result = 0.5;
        }

        double expectedOutcome = 1.0 / (1 + Math.pow(10, (opponent.getScore() - this.getScore()) / 400.0));

        int ratingChange = (int) (K * (result - expectedOutcome));

        this.setScore(this.getScore() + ratingChange);

        if (result == 1.0) {
            wins++;
        } else if (result == 0.5) {
            draws++;
        } else if (result == 0){
            loses++;
        } else {
            throw new IllegalStateException("Wrong game state when updating elo");
        }
    }
}