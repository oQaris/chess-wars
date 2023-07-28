package io.deeplay.service;

import io.deeplay.domain.GameType;
import io.deeplay.engine.SelfPlay;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;

import java.nio.charset.Charset;
import java.util.Scanner;

public class UserCommunicationService {

    public static int chooseBotLevel() {
        // Scanner или с помощью интерфейса вводить число от 1 до 3

        return 1;
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

    public static SelfPlay getGameInfo() {
        // С начальной страницы получить тип игры
        System.out.println("Game opened...");
        Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());

        char inputType = 'c';

        System.out.println("choose type of the game (bot-bot / human-human / human-bot)");
        String gameType = scanner.nextLine();

        if (gameType.equals("bot-bot")) {
            return new SelfPlay(new Bot('w', UserCommunicationService.chooseBotLevel()), new Bot('b', UserCommunicationService.chooseBotLevel()), GameType.BotVsBot);
        } else if (gameType.equals("human-human")) {
            char[] userColor = UserCommunicationService.chooseColor();
            return new SelfPlay(new Human(userColor[0]), new Human(userColor[1]), GameType.HumanVsHuman);
        } else if (gameType.equals("human-bot")) {
            char[] userColor = UserCommunicationService.chooseColor();
            return new SelfPlay(new Human(userColor[0]), new Bot(userColor[1], UserCommunicationService.chooseBotLevel()), GameType.HumanVsBot);
        } else {
            System.out.println("Invalid input. Ending...");
            System.exit(0);
        }
        return null;
    }
}
