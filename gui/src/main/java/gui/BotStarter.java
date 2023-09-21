package gui;

import io.deeplay.client.Client;
import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.domain.Color;
import io.deeplay.domain.GameStates;
import io.deeplay.engine.GameInfo;
import io.deeplay.engine.GameState;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BotStarter implements EndpointUser {
    private boolean isPlayerMove;
    private final GameInfo gameInfo;
    private final Player player;
    private final Client client;

    /**
     * Конструктор класса. Задает начальные параметры переменных.
     * Создает нового клиента и создает соединение с сервером.
     *
     * @param startGameDTO начальные параметры с главной страницы, выбранные пользователем
     */
    public BotStarter(StartGameDTO startGameDTO) {
        Color currentColor = Converter.convertColor(startGameDTO.getCurrentColor());
        isPlayerMove = currentColor == io.deeplay.domain.Color.WHITE;
        this.gameInfo = new GameInfo(currentColor) {
            @Override
            protected void changeCurrentMoveColor() {

            }
        };

        this.player = Converter.createNewBot(startGameDTO.getBotType(), currentColor);
        this.client = new Client(startGameDTO);
        client.connectToServer();

        if (!isPlayerMove) {
            waitAndUpdate();
        }
    }

    /**
     * Если ход игрока - получает выбранный ботом ход. Добавляет этот ход в GameInfo, отправляет ход серверу
     * и переходит в ожидание запроса от сервера.
     */
    @Override
    public void initialize() {
        while (true) {
            if (isPlayerMove) {
                Move move = player.getMove(gameInfo.getCurrentBoard(), player.getColor());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                gameInfo.move(move);
                List<Object> error = GameState.getErrorList();

                if (!error.isEmpty()) {
                    List<Object> trimmedError = error.subList(0, 2);
                    client.sendErrorToServer(trimmedError);
                } else {
                    client.sendMove(move);
                    isPlayerMove = false;
                }
            }

            waitAndUpdate();
        }
    }

    /**
     * Метод обрабатывает конец игры
     *
     * @param endGameInfo информация об окончании игры
     */
    @Override
    public void endGame(List<String> endGameInfo) {
        String gameStates = endGameInfo.get(0);
        String winColor;
        JLabel winColorLabel = new JLabel();

        if (!gameStates.equals(GameStates.DRAW.toString())) {
            winColor = endGameInfo.get(1);
            winColorLabel = new JLabel("Победитель: " + winColor);
        }

        JFrame frame = new JFrame("Игра завершена");
        JLabel gameStatesLabel = new JLabel("Состояние игры: " + gameStates);

        JPanel panel = new JPanel();
        panel.add(gameStatesLabel);

        if (!gameStates.equals(GameStates.DRAW.toString())) {
            panel.add(winColorLabel);
        }

        frame.getContentPane().add(panel);
        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Метод ожидает запрос от сервера, а затем вызывает функцию обработки этого запроса.
     */
    @Override
    public void waitAndUpdate() {
        Object playerAction = client.startListening();
        processClientInfo(playerAction);
    }

    /**
     * Обновляет GameInfo, передает ход текущему игроку.
     *
     * @param incomingMove ход противоположного игрока
     */
    @Override
    public void updateGameInfo(Move incomingMove) {
        gameInfo.move(incomingMove);
        isPlayerMove = true;
    }

    public void processClientInfo(Object action) {
        if (action instanceof Move move) {
            updateGameInfo(move);
        } else if (action instanceof List<?> list) {
            if (list.get(0) instanceof String) {
                List<String> endGameInfo = (List<String>) list;
                endGame(endGameInfo);
            } else if (list.get(0) instanceof Exception) {
                List<Object> errorGame = (List<Object>) list;

                endGameWithError(errorGame);
            }
        }
    }

    private void endGameWithError(List<Object> errorGame) {
        Exception exception = (Exception) errorGame.get(0);
        String message = (String) errorGame.get(1);
        String errorMessage = "Произошла ошибка во время игры: " + message + "\n" + exception.getMessage();

        JFrame frame = new JFrame("Ошибка");
        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JOptionPane.showMessageDialog(frame, errorMessage, "Ошибка", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}