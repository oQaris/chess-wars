package io.deeplay.service;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;

import java.nio.charset.Charset;
import java.util.Scanner;

public class UserCommunicationService {

    public static int chooseBotLevel() {
        // Scanner или с помощью интерфейса вводить число от 1 до 3

        return 1;
    }

    public static Color[] chooseColor() {
        Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());
        while (true) {
            System.out.println("choose color of player1");
            char userInput = scanner.next().charAt(0);
            if (userInput == 'w') {
                System.out.println("player1 color is set to white");
                System.out.println("player2 color is set to black");

                Color[] chosenColors = new Color[2];
                chosenColors[0] = Color.WHITE;
                chosenColors[1] = Color.BLACK;
                return chosenColors;
            } else if (userInput == 'b'){
                System.out.println("player1 color is set to black");
                System.out.println("player2 color is set to white");

                Color[] chosenColors = new Color[2];
                chosenColors[0] = Color.BLACK;
                chosenColors[1] = Color.WHITE;
                return chosenColors;
            }
        }
    }

    public static GameSession getGameSessionInfo() {
        // С начальной страницы получить тип игры
        System.out.println("Game opened...");
        Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());

        char inputType = 'c';

        System.out.println("choose type of the game (bot-bot / human-human / human-bot)");
        String gameType = scanner.nextLine();

        if (gameType.equals("bot-bot")) {
            return new GameSession(new Bot(Color.WHITE, UserCommunicationService.chooseBotLevel()),
                    new Bot(Color.BLACK, UserCommunicationService.chooseBotLevel()), GameType.BotVsBot);
        } else if (gameType.equals("human-human")) {
            Color[] userColor = UserCommunicationService.chooseColor();
            return new GameSession(new Human(userColor[0]), new Human(userColor[1]), GameType.HumanVsHuman);
        } else if (gameType.equals("human-bot")) {
            Color[] userColor = UserCommunicationService.chooseColor();
            return new GameSession(new Human(userColor[0]), new Bot(userColor[1],
                    UserCommunicationService.chooseBotLevel()), GameType.HumanVsBot);
        } else {
            System.out.println("Invalid input. Ending...");
            System.exit(0);
        }
        return null;
    }
}
