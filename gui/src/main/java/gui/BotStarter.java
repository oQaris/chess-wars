package gui;

import io.deeplay.client.Client;
import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.domain.Color;
import io.deeplay.engine.GameInfo;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Player;

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
                client.sendMove(move);
                isPlayerMove = false;
            }

            waitAndUpdate();
        }
    }

    /**
     * Метод обрабатывает конец игры
     * @param endGameInfo информация об окончании игры
     */
    public void endGame(List<String> endGameInfo) {

    }

    /**
     * Метод ожидает запрос от сервера, а затем вызывает функцию обработки этого запроса.
     */
    public void waitAndUpdate() {
        Move move = (Move) client.startListening();
        updateGameInfo(move);
    }

    /**
     * Обновляет GameInfo, передает ход текущему игроку.
     * @param incomingMove ход противоположного игрока
     */
    public void updateGameInfo(Move incomingMove) {
        gameInfo.move(incomingMove);
        isPlayerMove = true;
    }
}
