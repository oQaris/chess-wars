package io.deeplay.service;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.*;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

public class UserCommunicationService {

    private static final Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());

    public static int chooseBotLevel() {
        // Scanner или с помощью интерфейса вводить число от 1 до 3

        return 1;
    }

    public static Color[] chooseColor() {
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
            return null;
        }
    }

    public static Piece selectPiece(List<Piece> possiblePiecesToMove) {
        Piece selectedPiece = null;

        while (selectedPiece == null) {
            System.out.println("choose piece (its number) which you want to move:");
            for (int i = 0; i < possiblePiecesToMove.size(); i++) {
                System.out.println("(" + i + ") " + possiblePiecesToMove.get(i).getColor().name() + " "
                        + possiblePiecesToMove.get(i).getClass().getSimpleName()
                        +" at x:" + possiblePiecesToMove.get(i).getCoordinates().getX()
                        + " y:" + possiblePiecesToMove.get(i).getCoordinates().getY());
            }
            selectedPiece = possiblePiecesToMove.get(scanner.nextInt());
        }
        return selectedPiece;
    }

    public static Coordinates selectCoordinates(List<Coordinates> availableMoves) {
        Coordinates moveCoordinates = null;

        while (moveCoordinates == null) {
            System.out.println("choose coordinates in which you want to move your piece:");
            for (int i = 0; i < availableMoves.size(); i++) {
                System.out.println("(" + i + ") x: " + availableMoves.get(i).getX()
                        + " y: " + availableMoves.get(i).getY());
            }

            moveCoordinates = availableMoves.get(scanner.nextInt());
        }
        return moveCoordinates;
    }

    public static SwitchPieceType selectSwitchPiece() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите новую фигуру: ");
        System.out.println("1. Queen");
        System.out.println("2. Rook");
        System.out.println("3. Bishop");
        System.out.println("4. Knight");
        int choice = scanner.nextInt();

        return switch (choice) {
            case 1 -> SwitchPieceType.QUEEN;
            case 2 -> SwitchPieceType.ROOK;
            case 3 -> SwitchPieceType.BISHOP;
            case 4 -> SwitchPieceType.KNIGHT;
            default -> throw new IllegalArgumentException("Invalid choice: " + choice);
        };
    }
}
