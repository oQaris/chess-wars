package io.deeplay;

import io.deeplay.engine.GameSession;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.utils.BoardUtils;
import io.deeplay.service.UserCommunicationService;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
//
        Board board = new Board();
        BoardUtils utils = new BoardUtils();
//
//        board.printBoard();
//        board.movePiece(48, 40);
//
//        System.out.println();
//        System.out.println();
//        board.printBoard();
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++){
                utils.render(board, board.getPiece(new Coordinates(i,j)));
            }
        }

//        GameSession gameSession = UserCommunicationService.getGameSessionInfo();
//        gameSession.startGameSession();
    }
}