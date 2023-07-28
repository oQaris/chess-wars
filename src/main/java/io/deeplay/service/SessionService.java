package io.deeplay.service;

import io.deeplay.engine.SelfPlay;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;

import java.nio.charset.Charset;
import java.util.Scanner;

public class SessionService {

    public static SelfPlay openGameAndGetInfo() {
        // С начальной страницы получить тип игры
        System.out.println("Game opened...");
        Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());

        char inputType = 'c';

        System.out.println("choose type of the game (bot-bot / human-human / human-bot)");
        String gameType = scanner.nextLine();

        if (gameType.equals("bot-bot")) {
            return new SelfPlay(new Bot('w', UserCommunicationService.chooseBotLevel()), new Bot('b', UserCommunicationService.chooseBotLevel()));
        } else if (gameType.equals("human-human")) {
            char[] userColor = UserCommunicationService.chooseColor();
            return new SelfPlay(new Human(userColor[0]), new Human(userColor[1]));
        } else if (gameType.equals("human-bot")) {
            char[] userColor = UserCommunicationService.chooseColor();
            return new SelfPlay(new Human(userColor[0]), new Bot(userColor[1], UserCommunicationService.chooseBotLevel()));
        } else {
            System.out.println("Invalid input. Ending...");
            System.exit(0);
        }
        return null;
    }

    public static void startGameSession(SelfPlay selfPlay) {

    }
}
