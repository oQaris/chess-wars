package io.deeplay.engine;

import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Piece;

public class GameState {
    public static boolean check(Board board) {

        return false;
    }
    public static int checkMaterialSum(Board board){
        var res = 0;
        for(int i = 0; i < 8; i ++){
            for(int j = 0; j < 8; j ++){
                switch (board.getPiece(new Coordinates(i,j)).getClass().getSimpleName()){
                    case "Pawn" -> res+= 1;
                    case "Knight" -> res+= 3;
                    case "Bishop" -> res+= 3;
                    case "Rook" -> res+= 5;
                    case "Queen" -> res+= 9;
                    case "King" -> res+= 0;
                }
            }
        }
        return res;
    }
}
