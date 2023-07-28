package io.deeplay.engine;

import io.deeplay.domain.GameType;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;
import io.deeplay.service.SessionService;

// Класс должен иметь одну цель - самостоятельно понимать кто ходит,
// а также записывать в лог каждый ход
public class SelfPlay {

    private Player player1;
    private Player player2;
    private GameType gameType;

    public SelfPlay(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    // метод выбирает, кто ходит в данный момент
    public void chooseMove() {

    }

    private GameType checkGameType() {
        System.out.println("checking game type...");
        if (player1 instanceof Bot && player2 instanceof Bot) return GameType.BotVsBot;
        else if (player1 instanceof Human && player2 instanceof Human) return GameType.HumanVsHuman;
        else return GameType.HumanVsBot;
    }
}
