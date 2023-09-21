package io.deeplay.henryAI;
import io.deeplay.domain.Color;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Empty;

public class GreedyStrategy implements Strategy{
    private final Color maximizingColor;

    public GreedyStrategy(Color maximizingColor) {
        this.maximizingColor = maximizingColor;
    }

    public Color getMaximizingColor(){
        return this.maximizingColor;
    }

    @Override
    public int evaluate(Board board, Color color) {
        if (GameState.isMate(board, color)) {
            if (color == getMaximizingColor()) {
                return Integer.MIN_VALUE + 1;
            } else if (color == getMaximizingColor().opposite()) {
                return Integer.MAX_VALUE - 1;
            }
        } else if (GameState.drawWithGameWithoutTakingAndAdvancingPawns(board) || GameState.isStaleMate(board, color)) {
            return 0;
        }
        int res = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!(board.getPiece(new Coordinates(i, j)) instanceof Empty)) {
                    if (board.getPiece(new Coordinates(i, j)).getColor().equals(color)) {
                        switch (board.getPiece(new Coordinates(i, j)).getClass().getSimpleName()){
                            case ("Pawn") -> res += 10;
                            case ("Bishop"), ("Knight") -> res += 30;
                            case ("Rook") -> res += 50;
                            case ("Queen") -> res += 90;
                            case ("King") -> res += 1000;
                        }
                    }
                    if (!board.getPiece(new Coordinates(i, j)).getColor().equals(color)) {
                        switch (board.getPiece(new Coordinates(i, j)).getClass().getSimpleName()){
                            case ("Pawn") -> res -= 10;
                            case ("Bishop"), ("Knight") -> res -= 30;
                            case ("Rook") -> res -= 50;
                            case ("Queen") -> res -= 90;
                            case ("King") -> res -= 1000;
                        }
                    }
                }
            }
        }
        return res;
    }
}
