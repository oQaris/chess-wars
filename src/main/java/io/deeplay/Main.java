package io.deeplay;

import io.deeplay.engine.SelfPlay;
import io.deeplay.model.Board;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import io.deeplay.service.UserCommunicationService;

import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
//
//        Board board = new Board();
//
//        board.printBoard();
//        board.movePiece(48, 40);
//
//        System.out.println();
//        System.out.println();
//        board.printBoard();

        // С начальной страницы получить тип игры,

        System.out.println("Game opened...");

        Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());

        // Временно выбор всегда консоли
        char inputType = 'c';

        System.out.println("choose type of the game (bot-bot / human-human / human-bot)");
        String gameType = scanner.nextLine();

        if (gameType.equals("bot-bot")) {
            SelfPlay selfPlay = new SelfPlay(new Bot('w', UserCommunicationService.chooseBotLevel()), new Bot('b', UserCommunicationService.chooseBotLevel()));
            selfPlay.startGame();
        } else if (gameType.equals("human-human")) {
            char[] userColor = UserCommunicationService.chooseColor();
            SelfPlay selfPlay = new SelfPlay(new Human(userColor[0]), new Human(userColor[1]));
            selfPlay.startGame();
        } else if (gameType.equals("human-bot")) {
            char[] userColor = UserCommunicationService.chooseColor();
            SelfPlay selfPlay = new SelfPlay(new Human(userColor[0]), new Bot(userColor[1], UserCommunicationService.chooseBotLevel()));
            selfPlay.startGame();
        } else {
            System.out.println("Invalid input. Ending...");
            System.exit(0);
        }
    }
}
