package io.deeplay.marinaAI.bot;

import io.deeplay.domain.Color;
import io.deeplay.engine.GameState;
import io.deeplay.marinaAI.strategy.MaterialStrategy;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import io.deeplay.model.player.Bot;
import io.deeplay.service.GuiUserCommunicationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ExpectiMaxBotTest {
    @Test
    void testCheckmateInTwoMoves() {
        ExpectiMaxBot expectiMaxBot = new ExpectiMaxBot(Color.WHITE, 1, new GuiUserCommunicationService());
        Bot enemyBot = new Bot(Color.BLACK, 1, new GuiUserCommunicationService());
        Board board = new Board(getCheckmateInTwoMovesBoard1());

        Board.printBoardOnce(board);

        Move bestMove = expectiMaxBot.getMove(board, expectiMaxBot.getColor());
        board.move(bestMove);
        System.out.println(bestMove);
        Board.printBoardOnce(board);

        Move randomMove = enemyBot.getMove(board, enemyBot.getColor());
        board.move(randomMove);
        System.out.println(randomMove);
        Board.printBoardOnce(board);

        Move miniMaxMove2 = expectiMaxBot.getMove(board, expectiMaxBot.getColor());
        board.move(miniMaxMove2);
        System.out.println(miniMaxMove2);
        Board.printBoardOnce(board);

        Assertions.assertTrue(GameState.isMate(board, Color.BLACK));
    }

    @Test
    void testCheckmateInTwoMoves2() {
        ExpectiMaxBot expectiMaxBot = new ExpectiMaxBot(Color.WHITE, 1, new GuiUserCommunicationService());
        Bot enemyBot = new Bot(Color.BLACK, 1, new GuiUserCommunicationService());
        Board board = new Board(getCheckmateInTwoMovesBoard2());

        Board.printBoardOnce(board);

        Move bestMove = expectiMaxBot.getMove(board, expectiMaxBot.getColor());
        board.move(bestMove);
        System.out.println(bestMove);
        Board.printBoardOnce(board);

        Move randomMove = enemyBot.getMove(board, enemyBot.getColor());
        board.move(randomMove);
        System.out.println(randomMove);
        Board.printBoardOnce(board);

        Move miniMaxMove2 = expectiMaxBot.getMove(board, expectiMaxBot.getColor());
        board.move(miniMaxMove2);
        System.out.println(miniMaxMove2);
        Board.printBoardOnce(board);

        Assertions.assertTrue(GameState.isMate(board, Color.BLACK));
    }

    @Test
    void testTakePiece() {
        ExpectiMaxBot expectiMaxBot = new ExpectiMaxBot(Color.WHITE, 1, new GuiUserCommunicationService());
        Board board = new Board(getTakePieceBoard());

        Board.printBoardOnce(board);
        MaterialStrategy materialStrategy = new MaterialStrategy(expectiMaxBot.getColor());

        int evaluationScoreBeforeMove = materialStrategy.getMaterialScore(board);

        Move bestMove = expectiMaxBot.getMove(board, expectiMaxBot.getColor());
        board.move(bestMove);
        System.out.println(bestMove);
        Board.printBoardOnce(board);

        int evaluationScoreAfterMove = materialStrategy.getMaterialScore(board);

        Assertions.assertEquals(Math.abs(evaluationScoreAfterMove - evaluationScoreBeforeMove), 30);
    }

    @Disabled
    @Test
    void testStaleMate() {
        ExpectiMaxBot expectiMaxBot = new ExpectiMaxBot(Color.WHITE, 1, new GuiUserCommunicationService());
        MiniMaxBot enemyBot = new MiniMaxBot(Color.BLACK, 1, new GuiUserCommunicationService());
        Board board = new Board(getStalemateBoard());

        Board.printBoardOnce(board);

        Move bestMove = expectiMaxBot.getMove(board, expectiMaxBot.getColor());
        board.move(bestMove);
        System.out.println(bestMove);
        Board.printBoardOnce(board);

        Move randomMove = enemyBot.getMove(board, enemyBot.getColor());
        board.move(randomMove);
        System.out.println(randomMove);
        Board.printBoardOnce(board);

        Assertions.assertTrue(GameState.isStaleMate(board, Color.WHITE));
    }

    private Piece[][] getCheckmateInTwoMovesBoard1() {
        Piece[][] board = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Empty(new Coordinates(i, j));
            }
        }

        board[3][5] = new King(new Coordinates(3, 5), Color.WHITE);
        board[2][7] = new Queen(new Coordinates(2, 7), Color.WHITE);
        board[4][3] = new Knight(new Coordinates(4, 3), Color.WHITE);
        board[5][2] = new Bishop(new Coordinates(5, 2), Color.WHITE);

        board[5][6] = new King(new Coordinates(5, 6), Color.BLACK);
        board[6][6] = new Pawn(new Coordinates(6, 6), Color.BLACK);

        return board;
    }

    private Piece[][] getCheckmateInTwoMovesBoard2() {
        Piece[][] board = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Empty(new Coordinates(i, j));
            }
        }

        board[0][3] = new King(new Coordinates(0, 3), Color.BLACK);
        board[4][0] = new Bishop(new Coordinates(4, 0), Color.BLACK);
        board[7][2] = new Rook(new Coordinates(7, 2), Color.BLACK);

        board[1][0] = new Queen(new Coordinates(1, 0), Color.WHITE);
        board[2][1] = new Pawn(new Coordinates(2, 1), Color.WHITE);
        board[0][7] = new King(new Coordinates(0, 7), Color.WHITE);
        board[1][7] = new Rook(new Coordinates(1, 7), Color.WHITE);

        return board;
    }

    public Piece[][] getTakePieceBoard() {
        Piece[][] board = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Empty(new Coordinates(i, j));
            }
        }

        board[0][7] = new Rook(new Coordinates(0, 7), Color.BLACK);
        board[7][7] = new Rook(new Coordinates(7, 7), Color.BLACK);
        board[5][7] = new King(new Coordinates(5, 7), Color.BLACK);
        board[0][3] = new Pawn(new Coordinates(0, 3), Color.BLACK);
        board[1][3] = new Pawn(new Coordinates(1, 3), Color.BLACK);
        board[2][5] = new Pawn(new Coordinates(2, 5), Color.BLACK);
        board[5][5] = new Pawn(new Coordinates(5, 5), Color.BLACK);
        board[5][6] = new Pawn(new Coordinates(5, 6), Color.BLACK);
        board[7][6] = new Pawn(new Coordinates(7, 6), Color.BLACK);
        board[2][4] = new Knight(new Coordinates(2, 4), Color.BLACK);
        board[7][5] = new Knight(new Coordinates(7, 5), Color.BLACK);

        board[3][5] = new Knight(new Coordinates(3, 5), Color.WHITE);
        board[5][2] = new Knight(new Coordinates(5, 2), Color.WHITE);
        board[0][1] = new Pawn(new Coordinates(0, 1), Color.WHITE);
        board[4][2] = new Pawn(new Coordinates(4, 2), Color.WHITE);
        board[5][1] = new Pawn(new Coordinates(5, 1), Color.WHITE);
        board[6][1] = new Pawn(new Coordinates(6, 1), Color.WHITE);
        board[7][1] = new Pawn(new Coordinates(7, 1), Color.WHITE);
        board[4][1] = new King(new Coordinates(4, 1), Color.WHITE);
        board[7][0] = new Rook(new Coordinates(7, 0), Color.WHITE);
        board[2][0] = new Rook(new Coordinates(2, 0), Color.WHITE);

        return board;
    }

    private Piece[][] getStalemateBoard() {
        Piece[][] board = new Piece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Empty(new Coordinates(i, j));
            }
        }

        board[4][3] = new King(new Coordinates(4, 3), Color.BLACK);
        board[2][1] = new Pawn(new Coordinates(2, 1), Color.BLACK);
        board[1][7] = new Bishop(new Coordinates(1, 7), Color.BLACK);

        board[0][1] = new Bishop(new Coordinates(0, 1), Color.WHITE);
        board[0][7] = new King(new Coordinates(0, 7), Color.WHITE);

        return board;
    }
}