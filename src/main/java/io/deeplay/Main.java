package io.deeplay;

import io.deeplay.engine.GameSession;
import io.deeplay.model.Board;
import io.deeplay.service.UserCommunicationService;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
//
//        Board board = new Board();
//
//        board.printBoard();
//        board.movePiece(48, 40);
//
//        System.out.println();
//        System.out.println();
//        board.printBoard();

        GameSession gameSession = UserCommunicationService.getGameSessionInfo();
        gameSession.startGameSession();
    }
}
