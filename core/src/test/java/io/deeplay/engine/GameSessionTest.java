package io.deeplay.engine;

import io.deeplay.domain.Color;
import io.deeplay.domain.GameType;
import io.deeplay.igorAI.MinimaxBot;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;
import io.deeplay.service.GuiUserCommunicationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    @Test
    void choosePlayer() {
        Player player1 = new Human(Color.WHITE, new GuiUserCommunicationService());
        Player player2 = new MinimaxBot(Color.BLACK, 1, new GuiUserCommunicationService());
        final GameSession gameSession = new GameSession(player1, player2, GameType.HumanVsBot);

        assertEquals(player1, gameSession.choosePlayer(Color.WHITE));
        assertEquals(player2, gameSession.choosePlayer(Color.BLACK));

        assertNotEquals(player1, gameSession.choosePlayer(Color.BLACK));
    }
}