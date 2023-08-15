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
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserCommunicationService {

    private final Scanner scanner;
    private final PrintStream printStream;

    public UserCommunicationService(InputStream inputStream, PrintStream printStream) {
        this.scanner = new Scanner(inputStream, Charset.defaultCharset().name());
        this.printStream = printStream;
    }

    /**
     * Метод дает пользователю вариант выбора типа игры и возвращает новый объект игровой сессии.
     *
     * @return новый объект игровой сессии
     */
    public GameSession getGameSessionInfo() {
        // С начальной страницы получить тип игры
        printStream.println("Game opened...");
        printStream.println("choose type of the game (bot-bot / human-human / human-bot)");
        String gameType = scanner.nextLine();

        switch (gameType) {
            case "bot-bot" -> {
                return new GameSession(new Bot(Color.WHITE, chooseBotLevel()),
                        new Bot(Color.BLACK, chooseBotLevel()), GameType.BotVsBot);
            }
            case "human-human" -> {
                Color[] userColor = chooseColor();
                return new GameSession(new Human(userColor[0]), new Human(userColor[1]), GameType.HumanVsHuman);
            }
            case "human-bot" -> {
                Color[] userColor = chooseColor();
                return new GameSession(new Human(userColor[0]), new Bot(userColor[1],
                        chooseBotLevel()), GameType.HumanVsBot);
            }
            default -> {
                throw new IllegalArgumentException("Invalid input. Ending...");
            }
        }
    }

    /**
     * Метод дает пользователю выбрать фигуру, которой он хочет походить, из доступных для хода,
     * и возвращает выбранную фигуру.
     *
     * @param possiblePiecesToMove лист из фигур, которыми можно походить
     * @return выбранную игроком фигуру
     */
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

            int selectedMove = 0;
            try {
                selectedMove = scanner.nextInt();
            } catch(InputMismatchException ime){
                System.out.println("Selection must be an integer! Please try again:");
            }

            if (selectedMove > possiblePiecesToMove.size() || selectedMove < 0)
                throw new IllegalArgumentException("Invalid choice!");
            else selectedPiece = possiblePiecesToMove.get(selectedMove);
        }
        return selectedPiece;
    }

    /**
     * Метод дает пользователю выбрать координаты фигуры из доступных для хода и возвращает выбранные координаты.
     *
     * @param availableMoves доступные координаты фигуры для хода
     * @return выбранные игроком координаты
     */
    public Coordinates selectCoordinates(List<Coordinates> availableMoves) {
        Coordinates moveCoordinates = null;

        while (moveCoordinates == null) {
            printStream.println("choose coordinates in which you want to move your piece:");
            for (int i = 0; i < availableMoves.size(); i++) {
                printStream.println("(" + i + ") x: " + availableMoves.get(i).getX()
                        + " y: " + availableMoves.get(i).getY());
            }
            int selectedMove = 0;
            try {
                selectedMove = scanner.nextInt();
            } catch(InputMismatchException ime){
                System.out.println("Selection must be an integer! Please try again:");
            }

            if (selectedMove > availableMoves.size() || selectedMove < 0)
                throw new IllegalArgumentException("Invalid choice!");
            else moveCoordinates = availableMoves.get(selectedMove);
        }
        return moveCoordinates;
    }

    /**
     * Метод дает игроку выбрать тип фигуры для promotion и возвращает выбранный тип.
     *
     * @return тип фигуры на promotion
     */
    public SwitchPieceType selectSwitchPiece() {
        printStream.println("Выберите новую фигуру: ");
        printStream.println("(1) Queen");
        printStream.println("(2) Rook");
        printStream.println("(3) Bishop");
        printStream.println("(4) Knight");
        int choice = scanner.nextInt();

        return switch (choice) {
            case 1 -> SwitchPieceType.QUEEN;
            case 2 -> SwitchPieceType.ROOK;
            case 3 -> SwitchPieceType.BISHOP;
            case 4 -> SwitchPieceType.KNIGHT;
            default -> throw new IllegalArgumentException("Invalid choice!");
        };
    }

    /**
     * Метод возвращает выбранный уровень сложности бота
     *
     * @return число - сложность бота
     */
    public int chooseBotLevel() {
        // Scanner или с помощью интерфейса вводить число от 1 до 3

        return 1;
    }

    /**
     * Метод дает игроку выбрать цвет фигур и возвращает выбранные цвета в виде массива.
     * 0 элемент массива - player1, 1 элемент массива - player2
     *
     * @return массив из выбранных пользователем цветов фигур игроков
     */
    public Color[] chooseColor() {
        printStream.println("choose color of player1 (w/b):");
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
        } else {
            throw new IllegalArgumentException("Invalid color choice!");
        }
    }

    /**
     * Метод дает выбор игроку - продолжать игру или нет
     *
     * @return продолжается игра или нет
     */
    public boolean continueToPlay() {
        printStream.println("Do you want to continue playing? (yes/no)");
        String userResponse = scanner.nextLine();
        if (userResponse.equals("yes")) return true;
        else if (userResponse.equals("no")) return false;
        else throw new IllegalArgumentException("Invalid continuing game choice!");
    }
}
