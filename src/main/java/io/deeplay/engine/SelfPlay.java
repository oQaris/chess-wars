package io.deeplay.engine;

import io.deeplay.domain.GameType;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;

// Класс должен иметь одну цель - самостоятельно понимать кто ходит,
// а также записывать в лог каждый ход
public class SelfPlay {

    private Player player1;
    private Player player2;
    private GameType gameType;

    public SelfPlay(Player player1, Player player2, GameType gameType) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameType = gameType;
    }

    // метод выбирает, кто ходит в данный момент
    public void chooseMove() {

    }
}
