package io.deeplay.engine;

import io.deeplay.model.Board;
import io.deeplay.model.move.Move;
import io.deeplay.model.move.MoveType;
import io.deeplay.model.player.Player;

import java.io.IOException;
import java.util.ArrayList;

import static io.deeplay.engine.ByteUtils.longToByte;
import static io.deeplay.engine.ByteUtils.longToBytes;

public class GameInfo {
    private Player player1;
    private Player player2;
    private ArrayList<Move> history = new ArrayList<>();

    public static ArrayList<Move> getAllPossibleMoves(Board board, Player player) throws IOException {
        ArrayList<Move> moves = new ArrayList<>();
        char playerColor = player.getPiecesColor();
        if (playerColor == 'b') {
            byte[] blackPawns = longToBytes(board.getBlackPawns());
            for (int i = 0; i < blackPawns.length; i++) {
                moves.addAll(getPawnPossibleMoves(board, i, 'b'));
            }
            byte[] blackKnights = longToBytes(board.getBlackKnights());
            for (int i = 0; i < blackKnights.length; i++) {
                moves.addAll(getKnightPossibleMoves(board, i, 'b'));
            }
        } else if (playerColor == 'w') {
            byte[] blackBishops = longToBytes(board.getBlackBishops());
            for (int i = 0; i < blackBishops.length; i++) {
                moves.addAll(getBishopPossibleMoves(board, i, 'w'));
            }
            byte[] blackRooks = longToBytes(board.getBlackRooks());
            for (int i = 0; i < blackRooks.length; i++) {
                moves.addAll(getRookPossibleMoves(board, i, 'b'));
            }
        } else throw new IOException();

        return moves;
    }

    // вынести ifы в разные методы
    public static ArrayList<Move> getPawnPossibleMoves(Board board, int square, char playerColor) throws IOException {
        ArrayList<Move> moves = new ArrayList<>();
        if (playerColor == 'w') {
            if ((board.getWhitePawns() & (1L << square)) != 0) {
                byte[] pawns = longToBytes(board.getWhitePawns());
                for (int i = 0; i < pawns.length; i++) {
                    if (whitePawnCanMove(square, i) && pawns[i] == 0x0) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if (whitePawnCanMove(square, i) && pawns[i] == 0x1) {
                        moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                    } else if (endOfTheBoard(i) && whitePawnCanMove(square, i)) {  // надо сделать красиво, думаю как, не бей
                        moves.add(new Move(square, i, MoveType.PROMOTION, (char) System.in.read()));
                    }
                }
            }
        }else if (playerColor == 'b') {
                if ((board.getBlackPawns() & (1L << square)) != 0) {
                    byte[] pawns = longToBytes(board.getBlackPawns());
                    for (int i = 0; i < pawns.length; i++) {
                        if (blackPawnCanMove(square, i) && pawns[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (blackPawnCanMove(square, i) && pawns[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (endOfTheBoard(i) && blackPawnCanMove(square, i)) {  // надо сделать красиво, думаю как, не бей
                            moves.add(new Move(square, i, MoveType.PROMOTION, (char) System.in.read()));
                        }
                    }
                }
            } else throw new IOException();

        return moves;
    }
        public static ArrayList<Move> getKnightPossibleMoves (Board board,int square, char playerColor) throws IOException {
            ArrayList<Move> moves = new ArrayList<>();
            if (playerColor == 'w') {
                if ((board.getWhiteKnights() & (1L << square)) != 0) {
                    byte[] whiteKnights = longToBytes(board.getWhiteKnights());
                    for (int i = 0; i < whiteKnights.length; i++) {
                        if (whiteKnightCanMove(square, i) && whiteKnights[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (whiteKnightCanMove(square, i) && whiteKnights[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        }
                    }
                }
            }else if (playerColor == 'b') {
                if ((board.getBlackKnights() & (1L << square)) != 0) {
                    byte[] blackKnights = longToBytes(board.getBlackKnights());
                    for (int i = 0; i < blackKnights.length; i++) {
                        if (blackKnightCanMove(square, i) && blackKnights[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (blackKnightCanMove(square, i) && blackKnights[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        }
                    }
                }
            } else throw new IOException();
            return moves;
        }
        public static ArrayList<Move> getBishopPossibleMoves (Board board,int square, char playerColor) throws IOException {
            ArrayList<Move> moves = new ArrayList<>();
            if (playerColor == 'w') {
                if ((board.getWhiteBishops() & (1L << square)) != 0) {
                    byte[] whiteBishops = longToBytes(board.getWhiteBishops());
                    for (int i = 0; i < whiteBishops.length; i++) {
                        if (whiteBishopCanMove(square, i) && whiteBishops[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (whiteBishopCanMove(square, i) && whiteBishops[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        }
                    }
                }
            } else if (playerColor == 'b') {
                if ((board.getBlackBishops() & (1L << square)) != 0) {
                    byte[] blackBishops = longToBytes(board.getBlackBishops());
                    for (int i = 0; i < blackBishops.length; i++) {
                        if (blackBishopCanMove(square, i) && blackBishops[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (blackBishopCanMove(square, i) && blackBishops[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        }
                    }
                }
            } else throw new IOException();
            return moves;
        }

        public static ArrayList<Move> getRookPossibleMoves (Board board,int square, char playerColor) throws IOException {
            ArrayList<Move> moves = new ArrayList<>();
            if (playerColor == 'w') {
                if ((board.getWhiteRooks() & (1L << square)) != 0) {
                    byte[] whiteRooks = longToBytes(board.getWhiteRooks());
                    for (int i = 0; i < whiteRooks.length; i++) {
                        if (whiteRookCanMove(square, i) && whiteRooks[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (whiteRookCanMove(square, i) && whiteRooks[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        }
                    }
                }
            } else if (playerColor == 'b') {
                if ((board.getBlackRooks() & (1L << square)) != 0) {
                    byte[] blackRooks = longToBytes(board.getBlackRooks());
                    for (int i = 0; i < blackRooks.length; i++) {
                        if (blackRookCanMove(square, i) && blackRooks[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (blackRookCanMove(square, i) && blackRooks[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        }
                    }
                }
            }else throw new IOException();
            return moves;
        }

        public static ArrayList<Move> getKingPossibleMoves (Board board,int square, char playerColor) throws IOException
        {
            ArrayList<Move> moves = new ArrayList<>();
            if (playerColor == 'w') {
                if ((board.getWhiteKing() & (1L << square)) != 0) {
                    byte[] whiteKing = longToBytes(board.getWhiteKing());
                    byte whiteKingLong = longToByte(board.getWhiteKing());
                    if (whiteKingLong == 0b0) {
                        throw new IOException();
                    }
                    for (int i = 0; i < whiteKing.length; i++) {
                        if (!isUnderCheck() && whiteKingCanMove(square, i) && whiteKing[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (!isUnderCheck() && whiteKingCanMove(square, i) && whiteKing[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (whiteKingCanMove(square, i) && !((whiteKingLong & 0x1000000000000000L) == 0x0L) && board.getWhiteRooks()) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        }
                    }
                }
            } else if (playerColor == 'b') {
                if ((board.getBlackKing() & (1L << square)) != 0) {
                    byte[] blackKing = longToBytes(board.getBlackKing());
                    byte blackKingLong = longToByte(board.getBlackKing());
                    if (blackKingLong == 0b0) {
                        throw new IOException();
                    }
                    for (int i = 0; i < blackKing.length; i++) {
                        if (!isUnderCheck() && blackKingCanMove(square, i) && blackKing[i] == 0x0) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (!isUnderCheck() && blackKingCanMove(square, i) && blackKing[i] == 0x1) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        } else if (blackKingCanMove(square, i) && !((blackKingLong & 0x1000000000000000L) == 0x0L) && board.getWhiteRooks()) {
                            moves.add(new Move(square, i, MoveType.ORDINARY, '\0'));
                        }
                    }
                }
            } else throw new IOException();
            return moves;
        }
}