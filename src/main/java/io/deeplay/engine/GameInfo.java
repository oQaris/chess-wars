package io.deeplay.engine;

import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveType;
import io.deeplay.model.player.Player;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static io.deeplay.engine.ByteUtils.longToByte;
import static io.deeplay.engine.ByteUtils.longToBytes;

public class GameInfo {
    Player player1;
    Player player2;
    ArrayList<Move> history = new ArrayList<>();

    public ArrayList<Move> getAllPossibleMoves(Board board, Player player) throws IOException {
        ArrayList<Move> moves = new ArrayList<>();
        char playerColor = player.getPiecesColor();
        if (playerColor == 'b') {
            byte[] pawns = longToBytes(board.getBlackPawns());
            for (int i = 0; i < pawns.length; i++) {
                moves.addAll(getPossibleMoves(board, i, 'b'));
            }
            byte[] blackKnights = longToBytes(board.getBlackKnights());
            for (int i = 0; i < pawns.length; i++) {
                moves.addAll(getPossibleMoves(board, i, 'b'));
            }
        } else if (playerColor == 'w') {
            byte[] pawns = longToBytes(board.getWhitePawns());
            for (int i = 0; i < pawns.length; i++) {
                moves.addAll(getPossibleMoves(board, i, 'w'));
            }
            byte[] whiteKnights = longToBytes(board.getWhiteKnights());
            for (int i = 0; i < pawns.length; i++) {
                moves.addAll(getPossibleMoves(board, i, 'b'));
            }
        } else throw new IOException();

        return moves;
    }

    public ArrayList<Move> getPossibleMoves(Board board, int square, char playerColor) throws IOException {
        ArrayList<Move> moves = new ArrayList<>();
        if (playerColor == 'w') {
            if ((board.getWhitePawns() & (1L << square)) != 0){
                byte[] pawns = longToBytes(board.getWhitePawns());
                for(int i = 0; i < pawns.length; i++){
                    if(whitePawnCanMove(square, i) && pawns[i] == 0x0) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if(whitePawnCanMove(square, i) && pawns[i] == 0x1) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if(endOfTheBoard(i) && whitePawnCanMove(square, i)) {  // надо сделать красиво, думаю как, не бей
                        moves.add(new Move(square, i, MoveType.PROMOTION, (char) System.in.read()));
                    }
                }

            }else if((board.getWhiteKnights() & (1L << square)) != 0){
                byte[] whiteKnights = longToBytes(board.getWhiteKnights());
                for(int i = 0; i < whiteKnights.length; i++){
                    if(whiteKnightCanMove(square, i) && whiteKnights[i] == 0x0) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if(whiteKnightCanMove(square, i) && whiteKnights[i] == 0x1) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    }
                }
            }else if((board.getWhiteBishops() & (1L << square)) != 0){
                byte[] whiteBishops = longToBytes(board.getWhiteBishops());
                for(int i = 0; i < whiteBishops.length; i++){
                    if(whiteBishopCanMove(square, i) && whiteBishops[i] == 0x0) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if(whiteBishopCanMove(square, i) && whiteBishops[i] == 0x1) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    }
                }
            }else if((board.getWhiteRooks() & (1L << square)) != 0){
                byte[] whiteRooks = longToBytes(board.getWhiteRooks());
                for(int i = 0; i < whiteRooks.length; i++){
                    if(whiteRookCanMove(square, i) && whiteRooks[i] == 0x0) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if(whiteRookCanMove(square, i) && whiteRooks[i] == 0x1) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    }
                }
            }else if((board.getWhiteKing() & (1L << square)) != 0){
                byte[] whiteKing = longToBytes(board.getWhiteKing());
                byte whiteKingLong = longToByte(board.getWhiteKing());
                if(whiteKingLong == 0b0){
                    throw new IOException();
                }
                for(int i = 0; i < whiteKing.length; i++){
                    if(!isUnderCheck() && whiteKingCanMove(square, i) && whiteKing[i] == 0x0) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if(!isUnderCheck() && whiteKingCanMove(square, i) && whiteKing[i] == 0x1) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if(whiteKingCanMove(square, i) && !((whiteKingLong & 0x1000000000000000L) == 0x0L) && board.getWhiteRooks()) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    }
                }
            }
        } else if (playerColor == 'b') {
            byte[] pawns = longToBytes(board.getWhitePawns());
        } else throw new IOException();

        return moves;
    }
}
