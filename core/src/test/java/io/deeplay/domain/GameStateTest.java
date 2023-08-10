package io.deeplay.domain;

import io.deeplay.engine.GameInfo;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Empty;
import io.deeplay.model.piece.King;
import io.deeplay.model.piece.Queen;
import io.deeplay.model.piece.Rook;
import io.deeplay.model.player.Human;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GameStateTest {
    @Test
    public void CheckMateTest() throws IOException {
        Board board = new Board();
        GameInfo gameInfo = new GameInfo(board, new Human(Color.BLACK), new Human(Color.WHITE));

        for(int i =0; i < 8; i ++){
            for(int j = 0; j <8; j++){
                board.setPiece(new Coordinates(i,j), new Empty(new Coordinates(i, j)));
            }
        }
        Board.printBoardOnce(board);
        board.setPiece(new Coordinates(1,0), new King(new Coordinates(1, 0) , Color.WHITE));
        board.setPiece(new Coordinates(4,1), new Queen(new Coordinates(4,1) , Color.BLACK));
        board.setPiece(new Coordinates(2,7), new Rook(new Coordinates(2, 7) , Color.BLACK));
        Board.printBoardOnce(board);
        board.getMoveHistory().addMove(new Move(new Coordinates(1,0), new Coordinates(0,0), MoveType.ORDINARY));
        Board.printBoardOnce(board);
        gameInfo.move(new Move(new Coordinates(1,0), new Coordinates(0,0), MoveType.ORDINARY));
        Board.printBoardOnce(board);
        board.getMoveHistory().addMove(new Move(new Coordinates(2,7), new Coordinates(2,0), MoveType.ORDINARY));
        gameInfo.move(new Move(new Coordinates(2,7), new Coordinates(2,0), MoveType.ORDINARY));
        Board.printBoardOnce(board);
        System.out.println(GameState.getGameState(board));
    }
}
