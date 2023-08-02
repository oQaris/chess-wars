package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveType;
import io.deeplay.model.piece.Knight;
import io.deeplay.model.piece.Piece;
import io.deeplay.service.UserCommunicationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BotTest {

    private List<Piece> possiblePiecesToMove;
    private Board board;

    @BeforeEach
    public void initialize() {
        board = new Board();
        possiblePiecesToMove = new ArrayList<>();
        possiblePiecesToMove.add(new Knight(new Coordinates(6, 0), Color.WHITE));
        possiblePiecesToMove.add(new Knight(new Coordinates(1, 0), Color.WHITE));
    }

    @Test
    void testRandomMove() {
        final Bot bot = new Bot(Color.WHITE, 1);
        Move result = bot.getMove(possiblePiecesToMove, board);

        Assertions.assertNotNull(result);
    }

    @Test
    void testSingleAvailableMove() {
        final Bot bot = new Bot(Color.WHITE, 1);
        possiblePiecesToMove.remove(1);

        Assertions.assertDoesNotThrow(() -> bot.getMove(possiblePiecesToMove, board));

        Move result = bot.getMove(possiblePiecesToMove, board);
        Assertions.assertNotNull(result);
    }
}