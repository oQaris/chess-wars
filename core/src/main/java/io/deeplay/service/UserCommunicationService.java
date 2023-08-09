package io.deeplay.service;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.*;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

public class UserCommunicationService {

    private Scanner scanner;
    private PrintStream printStream;

    public UserCommunicationService(InputStream inputStream, PrintStream printStream) {
        this.scanner = new Scanner(inputStream, Charset.defaultCharset());
        this.printStream = printStream;
    }

    public GameSession getGameSessionInfo() {
        // С начальной страницы получить тип игры
        printStream.println("Game opened...");

        char inputType = 'c';

        printStream.println("choose type of the game (bot-bot / human-human / human-bot)");
        String gameType = scanner.nextLine();

        if (gameType.equals("bot-bot")) {
            return new GameSession(new Bot(Color.WHITE, chooseBotLevel()),
                    new Bot(Color.BLACK, chooseBotLevel()), GameType.BotVsBot);
        } else if (gameType.equals("human-human")) {
            Color[] userColor = chooseColor();
            return new GameSession(new Human(userColor[0]), new Human(userColor[1]), GameType.HumanVsHuman);
        } else if (gameType.equals("human-bot")) {
            Color[] userColor = chooseColor();
            return new GameSession(new Human(userColor[0]), new Bot(userColor[1],
                    chooseBotLevel()), GameType.HumanVsBot);
        } else {
            printStream.println("Invalid input. Ending...");
            System.exit(0);
            return null;
        }
    }

    public Piece selectPiece(List<Piece> possiblePiecesToMove) {
        Piece selectedPiece = null;

        while (selectedPiece == null) {
            printStream.println("choose piece (its number) which you want to move:");
            for (int i = 0; i < possiblePiecesToMove.size(); i++) {
                printStream.println("(" + i + ") " + possiblePiecesToMove.get(i).getColor().name() + " "
                        + possiblePiecesToMove.get(i).getClass().getSimpleName()
                        +" at x:" + possiblePiecesToMove.get(i).getCoordinates().getX()
                        + " y:" + possiblePiecesToMove.get(i).getCoordinates().getY());
            }
            selectedPiece = possiblePiecesToMove.get(scanner.nextInt());
        }
        return selectedPiece;
    }

    public Coordinates selectCoordinates(List<Coordinates> availableMoves) {
        Coordinates moveCoordinates = null;

        while (moveCoordinates == null) {
            printStream.println("choose coordinates in which you want to move your piece:");
            for (int i = 0; i < availableMoves.size(); i++) {
                printStream.println("(" + i + ") x: " + availableMoves.get(i).getX()
                        + " y: " + availableMoves.get(i).getY());
            }

            moveCoordinates = availableMoves.get(scanner.nextInt());
        }
        return moveCoordinates;
    }

    public SwitchPieceType selectSwitchPiece() {
        Scanner scanner = new Scanner(System.in);
        printStream.println("Выберите новую фигуру: ");
        printStream.println("1. Queen");
        printStream.println("2. Rook");
        printStream.println("3. Bishop");
        printStream.println("4. Knight");
        int choice = scanner.nextInt();

        return switch (choice) {
            case 1 -> SwitchPieceType.QUEEN;
            case 2 -> SwitchPieceType.ROOK;
            case 3 -> SwitchPieceType.BISHOP;
            case 4 -> SwitchPieceType.KNIGHT;
            default -> throw new IllegalArgumentException("Invalid choice: " + choice);
        };
    }

    public int chooseBotLevel() {
        // Scanner или с помощью интерфейса вводить число от 1 до 3

        return 1;
    }

    public Color[] chooseColor() {
        while (true) {
            printStream.println("choose color of player1");
            char userInput = scanner.next().charAt(0);
            if (userInput == 'w') {
                printStream.println("player1 color is set to white");
                printStream.println("player2 color is set to black");

                Color[] chosenColors = new Color[2];
                chosenColors[0] = Color.WHITE;
                chosenColors[1] = Color.BLACK;
                return chosenColors;
            } else if (userInput == 'b'){
                printStream.println("player1 color is set to black");
                printStream.println("player2 color is set to white");

                Color[] chosenColors = new Color[2];
                chosenColors[0] = Color.BLACK;
                chosenColors[1] = Color.WHITE;
                return chosenColors;
            }
        }
    }
}
