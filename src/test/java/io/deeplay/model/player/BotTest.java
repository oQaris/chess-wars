package io.deeplay.model.player;

import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BotTest {

    private List<Move> possibleMoves;

    @BeforeEach
    public void initializeBoard() {
        possibleMoves = new ArrayList<>();
        possibleMoves.add(new Move(8, 16, MoveType.ORDINARY, '\0'));
        possibleMoves.add(new Move(9, 17, MoveType.ORDINARY, '\0'));
        possibleMoves.add(new Move(10, 18, MoveType.ORDINARY, '\0'));
        possibleMoves.add(new Move(11, 19, MoveType.ORDINARY, '\0'));
        possibleMoves.add(new Move(12, 20, MoveType.ORDINARY, '\0'));
    }

    @Test
    void testRandomMove() {
        final Bot bot = new Bot('w', 1);
        Move move = new Move(0, 0, MoveType.ORDINARY, '\0');
        Move randomMove = bot.getRandomMove(possibleMoves);

        Assertions.assertNotNull(randomMove);

        Assertions.assertNotEquals(move.getStartPosition(), randomMove.getStartPosition());
        Assertions.assertNotEquals(move.getEndPosition(), randomMove.getEndPosition());
    }
}