package io.deeplay.engine;

import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;

import java.nio.charset.Charset;
import java.util.Scanner;

public class SelfPlay {

    private Player player1;
    private Player player2;

    enum GameType {
        BotVsBot,
        HumanVsBot,
        HumanVsHuman
    }

    public SelfPlay(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void startGame() {
        System.out.println("Game started!");
    }

    public void startGameSession() {

    }

    public static char[] chooseColor() {
        Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());
        char[] userInput = new char[2];
        while (true) {
            System.out.println("choose color of player1");
            userInput[0] = scanner.next().charAt(0);
            if (userInput[0] == 'w') {
                System.out.println("player1 color is set to white");
                System.out.println("player2 color is set to black");
                userInput[1] = 'b';
                return userInput;
            } else if (userInput[0] == 'b'){
                System.out.println("player1 color is set to black");
                System.out.println("player2 color is set to white");
                userInput[1] = 'w';
                return userInput;
            }
        }
    }

    public static int chooseBotLevel() {
        // Scanner или с помощью интерфейса вводить число от 1 до 3

        return 1;
    }

    private GameType checkGameType() {
        System.out.println("checking game type...");
        if (player1 instanceof Bot && player2 instanceof Bot) return GameType.BotVsBot;
        else if (player1 instanceof Human && player2 instanceof Human) return GameType.HumanVsHuman;
        else return GameType.HumanVsBot;
    }
}
