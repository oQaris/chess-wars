package gui.service;

import io.deeplay.model.Board;
import io.deeplay.model.piece.Piece;

public class BoardService {

    public static Piece[][] getBoard(Board board) {
        return board.getBoard();
    }
}
