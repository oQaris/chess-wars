package io.deeplay;

import io.deeplay.engine.SelfPlay;
import io.deeplay.model.Board;
import io.deeplay.service.SessionService;

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

        SelfPlay selfPlay = SessionService.openGameAndGetInfo();
        SessionService.startGameSession(selfPlay);
    }
}