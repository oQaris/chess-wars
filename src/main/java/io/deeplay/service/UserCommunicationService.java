package io.deeplay.service;

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
}
