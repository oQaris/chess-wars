package gui.service;

public class GamePropertiesService {
    public static io.deeplay.domain.Color getColor(String whitePlayerChoice) {
        boolean playerIsWhite;
        switch (whitePlayerChoice) {
            case "Белый" -> playerIsWhite = true;
            case "Черный" -> playerIsWhite = false;
            default -> throw new IllegalArgumentException("Wrong white player selection");
        }

        if (playerIsWhite) return io.deeplay.domain.Color.WHITE;
        else return io.deeplay.domain.Color.BLACK;
    }

    public static int getBotLevel(String givenBotLevel) {
        int botLevel;
        switch (givenBotLevel) {
            case "Легкий" -> botLevel = 1;
            case "Средний" -> botLevel = 2;
            case "Сложный" -> botLevel = 3;
            default -> throw new IllegalArgumentException("Wrong Bot Level selection");
        }
        return botLevel;
    }
}
