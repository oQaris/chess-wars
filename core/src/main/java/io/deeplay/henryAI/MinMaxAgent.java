package io.deeplay.henryAI;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Pawn;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Player;
import io.deeplay.service.BoardUtil;
import io.deeplay.service.IUserCommunication;

import java.util.ArrayList;
import java.util.List;

import static io.deeplay.engine.GameState.isCheck;

public class MinMaxAgent extends Bot {
    private final Color maximizingColor;
    private final static int DEPTH = 2;
    private static Strategy strategy;

    public MinMaxAgent(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, difficultyLevel, iUserCommunication);
        maximizingColor = color;
        strategy = new GreedyStrategy(maximizingColor);
    }
    private static Board duplicateBoard(Board board) {
        Board duplicateBoard = new Board();
        BoardUtil.duplicateBoard(board).accept(duplicateBoard);

        return duplicateBoard;
    }


    public static int minimax(int depth, Board board, Color color, boolean isMaximizingPlayer, int alpha, int beta) {
        if (depth == 0 || GameState.drawWithGameWithoutTakingAndAdvancingPawns(board)
                || GameState.isMate(board, color)
                || GameState.isStaleMate(board, color)) {
            return strategy.evaluate(board, color);
        }
        Color maximizingColor = isMaximizingPlayer ? Color.WHITE : Color.BLACK;
        if (isMaximizingPlayer) {
            int maxScore = Integer.MIN_VALUE;

            List<Piece> pieces = getPiecesPossibleToMove(board, maximizingColor);

            for (Piece piece : pieces) {
                List<Move> possibleMoves = getAllValidMoves(piece, board);
                List<Move> movesWithoutCheck = getMovesWithoutMakingCheck(board, piece, getAllValidMoves(piece,board));
                possibleMoves.retainAll(movesWithoutCheck);

                for (Move move : possibleMoves) {
                    Board copy = duplicateBoard(board);

                    if (move.moveType() == MoveType.PROMOTION) {
                        for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                            Board promotionBoard = duplicateBoard(copy);
                            promotionBoard.move(move);

                            int score = minimax(depth - 1, promotionBoard, maximizingColor.opposite(), false,alpha, beta);

                            maxScore = Math.max(maxScore, score);
                            alpha = Math.max(alpha, score);

                            if (beta <= alpha) {
                                break;
                            }

                        }
                    }

                    copy.move(move);

                    int score = minimax(depth - 1, copy, maximizingColor.opposite(), false, alpha, beta);

                    maxScore = Math.max(maxScore, score);
                    alpha = Math.max(alpha, score);

                    if (beta <= alpha) {
                        break;
                    }
                }
                return maxScore;
            }
        } else {
            int minScore = Integer.MAX_VALUE;
            List<Piece> possiblePieces = getPiecesPossibleToMove(board, maximizingColor.opposite());

            for (Piece piece : possiblePieces) {
                List<Move> possibleMoves = getAllValidMoves(piece, board);
                List<Move> movesWithoutCheck = getMovesWithoutMakingCheck(board, piece, possibleMoves);
                possibleMoves.retainAll(movesWithoutCheck);

                for (Move move : possibleMoves) {
                    Board copy = duplicateBoard(board);

                    if (move.moveType() == MoveType.PROMOTION) {
                        for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                            Board promotionBoard = duplicateBoard(copy);
                            promotionBoard.move(move);

                            int score = minimax(depth - 1, copy, maximizingColor, true, alpha, beta );

                            minScore = Math.min(minScore, score);
                            beta = Math.min(beta, score);
                            if (beta <= alpha) {
                                break;
                            }
                        }
                    }

                    copy.move(move);
                    int score = minimax(depth - 1, copy, maximizingColor, true, alpha, beta);
                    minScore = Math.min(minScore, score);
                    beta = Math.min(beta, score);

                    if (beta <= alpha) {
                        break;
                    }

                }
            }
            return minScore;
        }
        return 0;
    }

    public static List<Move> getAllValidMoves(Piece piece, Board board){
        List<Move> res = new ArrayList<>();
        for (Coordinates coordinates: piece.getPossibleMoves(board)) {
            if (piece instanceof Pawn && coordinates.getY() == 7 && piece.getColor().equals(Color.WHITE) ){
                for (SwitchPieceType s: SwitchPieceType.values()) {
                    if(!s.equals(SwitchPieceType.NULL)){
                        res.add(new Move(piece.getCoordinates(), coordinates, MoveType.PROMOTION, s));
                    }
                }
            }
            if (piece instanceof Pawn && coordinates.getY() == 0 && piece.getColor().equals(Color.BLACK) ){
                for (SwitchPieceType s: SwitchPieceType.values()) {
                    if(!s.equals(SwitchPieceType.NULL)){
                        res.add(new Move(piece.getCoordinates(), coordinates, MoveType.PROMOTION, s));
                    }
                }
            }
            if (!getType(piece, coordinates, board).equals(MoveType.PROMOTION)) {
                res.add(new Move(piece.getCoordinates(), coordinates, getType(piece, coordinates, board), SwitchPieceType.NULL));
            }
        }
        return res;
    }

    public static List<Move> getMovesWithoutMakingCheck(Board board, Piece piece,
                                                               List<Move> potentialCoordinates) {
        List<Move> rightMoves = new ArrayList<>();
        Color color = piece.getColor();

        for (Move move : potentialCoordinates) {
            MoveType moveType = Player.getType(piece, move.endPosition(), board);
            if (moveType == MoveType.PROMOTION) {
                for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                    Board duplicateBoard = new Board();
                    BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                    SwitchPieceType switchPieceType = SwitchPieceType.values()[i];
                    duplicateBoard.move(new Move(piece.getCoordinates(), move.endPosition(),
                            moveType, switchPieceType));
                    if (!isCheck(duplicateBoard, color)) {
                        rightMoves.add(move);
                    }
                }
            } else {
                Board duplicateBoard = new Board();
                BoardUtil.duplicateBoard(board).accept(duplicateBoard);
                SwitchPieceType switchPieceType = SwitchPieceType.NULL;
                duplicateBoard.move(new Move(piece.getCoordinates(), move.endPosition(),
                        moveType, switchPieceType));

                if (!isCheck(duplicateBoard, color)) {
                    rightMoves.add(move);
                }
            }
        }

        return rightMoves;
    }

    @Override
    public Move getMove(Board board, Color color) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Piece> possiblePieces = getPiecesPossibleToMove(board, color);

        for (Piece piece : possiblePieces) {
            List<Move> possibleMoves = getAllValidMoves(piece, board);
            List<Move> movesWithoutCheck = getMovesWithoutMakingCheck(board, piece, possibleMoves);
            possibleMoves.retainAll(movesWithoutCheck);

            for (Move move : possibleMoves) {
                Board copy = duplicateBoard(board);

                if (move.moveType() == MoveType.PROMOTION) {
                    for (int i = 0; i < SwitchPieceType.values().length - 1; i++) {
                        Board promotionBoard = duplicateBoard(board);
                        promotionBoard.move(move);

                        int score = minimax(DEPTH - 1, promotionBoard, color, false,bestScore, Integer.MAX_VALUE);

                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = move;
                        }
                    }
                }
                copy.move(move);

                int score = minimax(DEPTH - 1, copy, color, false, bestScore, Integer.MAX_VALUE);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }
}
