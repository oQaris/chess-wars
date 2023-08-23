package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {
    @Test
    void getColor() {
        final Board board = new Board();
        assertEquals(Color.WHITE, board.getPiece(new Coordinates(4, 0)).getColor());
        assertEquals(Color.BLACK, board.getPiece(new Coordinates(4, 7)).getColor());
    }

    @Test
    void getPossibleMovesFromStartBoard() {
        final Board board = new Board();
        List<Coordinates> possibleMoves = board.getPiece(new Coordinates(4, 0)).getPossibleMoves(board);

        Assertions.assertEquals(0, possibleMoves.size());
    }

    @Test
    void getPossibleMovesFromCenter(){
        final Board board = new Board();
        King king = new King(new Coordinates(0,5), Color.WHITE);
        List<Coordinates> possibleMovesFromCenter = king.getPossibleMoves(board);
        Assertions.assertEquals(5, possibleMovesFromCenter.size());
    }

    @Test
    void canMoveToEmptyCell() {
        final Board board = new Board();
        King king = new King(new Coordinates(0,5), Color.WHITE);
        List<Coordinates> possibleMovesFromCenter = king.getPossibleMoves(board);
        assertTrue(possibleMovesFromCenter.contains(new Coordinates(1, 5)));
    }

    @Test
    void canMoveToCellWithAllyPiece() {
        final Board board = new Board();
        King king = new King(new Coordinates(0,5), Color.WHITE);
        board.setPiece(new Coordinates(0,4), new King(new Coordinates(0,4), Color.WHITE));
        assertFalse(king.canMoveAt(new Coordinates(0, 4), board));
    }

    @Test
    void canMoveAtEnemyPiece(){
        final Board board = new Board();
        King king = new King(new Coordinates(0,5), Color.WHITE);
        assertTrue(king.canMoveAt(new Coordinates(0,6), board));
    }

    @Test
    void canMoveOnCurrent(){
        final Board board = new Board();
        King king = new King(new Coordinates(0,5), Color.WHITE);
        assertFalse(king.canMoveAt(new Coordinates(0,5),board));
    }

    @Test
    void testPossibleMovesWithCastlingForWhite() {
        final Board board = new Board();
        Coordinates defaultWhiteKingCoordinates = new Coordinates(4,0);
        King whiteKing = new King(defaultWhiteKingCoordinates, Color.WHITE);

        board.move(new Move(new Coordinates(6,0), new Coordinates(7,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(0,6), new Coordinates(0,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(6,1), new Coordinates(6,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(0,5), new Coordinates(0,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,0), new Coordinates(6,1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(0,4), new Coordinates(0,3), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertTrue(board.getPiece(defaultWhiteKingCoordinates) instanceof King);
        assertSame(board.getPiece(defaultWhiteKingCoordinates).getColor(), Color.WHITE);
        assertEquals(2, whiteKing.getPossibleMoves(board).size());

        List<Coordinates> rookCoordinates = new ArrayList<>();
        rookCoordinates.add(new Coordinates(0, 0));
        rookCoordinates.add(new Coordinates(7, 0));

        board.move(new Move(new Coordinates(4,0), new Coordinates(5,0), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(1,6), new Coordinates(1,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,0), new Coordinates(4,0), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(1,5), new Coordinates(1,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        List<Coordinates> possibleMovesAfterKingMove = whiteKing.getPossibleMoves(board);

        assertEquals(1, possibleMovesAfterKingMove.size());
    }

    @Test
    void testPossibleMovesWithCastlingForBlack() {
        final Board board = new Board();
        Coordinates defaultBlackKingCoordinates = new Coordinates(4,7);
        King blackKing = new King(defaultBlackKingCoordinates, Color.BLACK);

        board.move(new Move(new Coordinates(6,0), new Coordinates(7,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6,7), new Coordinates(7,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(6,1), new Coordinates(6,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6,6), new Coordinates(6,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,0), new Coordinates(6,1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(5,7), new Coordinates(6,6), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertTrue(board.getPiece(defaultBlackKingCoordinates) instanceof King);
        assertSame(board.getPiece(defaultBlackKingCoordinates).getColor(), Color.BLACK);
        assertEquals(2, blackKing.getPossibleMoves(board).size());
    }

    @Test
    void testPossibleMovesWithCastling_cannotCastleForWhite() {
        final Board board = new Board();
        Coordinates defaultWhiteKingCoordinates = new Coordinates(4,0);
        King whiteKing = new King(defaultWhiteKingCoordinates, Color.WHITE);

        board.move(new Move(new Coordinates(6,0), new Coordinates(7,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(1,7), new Coordinates(1,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(6,1), new Coordinates(6,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(0,5), new Coordinates(0,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,0), new Coordinates(6,1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(0,4), new Coordinates(0,3), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,1), new Coordinates(5,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(4,6), new Coordinates(4,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,3), new Coordinates(4,4), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(3,7), new Coordinates(5,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        assertTrue(board.getPiece(defaultWhiteKingCoordinates) instanceof King);
        assertSame(board.getPiece(defaultWhiteKingCoordinates).getColor(), Color.WHITE);
        assertEquals(2, whiteKing.getPossibleMoves(board).size());
    }

    @Test
    void testGetCastleMovesForWhite_rightSide() {
        final Board board = new Board();
        Coordinates defaultWhiteKingCoordinates = new Coordinates(4,0);
        King whiteKing = new King(defaultWhiteKingCoordinates, Color.WHITE);

        board.move(new Move(new Coordinates(6,0), new Coordinates(7,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(0,6), new Coordinates(0,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(6,1), new Coordinates(6,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(0,5), new Coordinates(0,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,0), new Coordinates(6,1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(0,4), new Coordinates(0,3), MoveType.ORDINARY, SwitchPieceType.NULL));

        List<Coordinates> rookCoordinates = new ArrayList<>();
        rookCoordinates.add(new Coordinates(0, 0));
        rookCoordinates.add(new Coordinates(7, 0));

        assertTrue(board.getPiece(defaultWhiteKingCoordinates) instanceof King);
        assertSame(board.getPiece(defaultWhiteKingCoordinates).getColor(), Color.WHITE);

        List<Coordinates> possibleCastleMoves = whiteKing.getCastleMoves(Color.BLACK, 0, defaultWhiteKingCoordinates,
                rookCoordinates, board);

        assertEquals(1, possibleCastleMoves.size());
    }

    @Test
    void testGetCastleMovesForWhite_leftSide() {
        final Board board = new Board();
        Coordinates defaultWhiteKingCoordinates = new Coordinates(4,0);
        King whiteKing = new King(defaultWhiteKingCoordinates, Color.WHITE);

        board.move(new Move(new Coordinates(1,0), new Coordinates(0,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(7,6), new Coordinates(7,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(1,1), new Coordinates(1,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(7,5), new Coordinates(7,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(2,0), new Coordinates(1,1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(7,4), new Coordinates(7,3), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(3,1), new Coordinates(3,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6,6), new Coordinates(6,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(3,0), new Coordinates(3,1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6,5), new Coordinates(6,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        List<Coordinates> rookCoordinates = new ArrayList<>();
        rookCoordinates.add(new Coordinates(0, 0));
        rookCoordinates.add(new Coordinates(7, 0));

        assertTrue(board.getPiece(defaultWhiteKingCoordinates) instanceof King);
        assertSame(board.getPiece(defaultWhiteKingCoordinates).getColor(), Color.WHITE);

        List<Coordinates> possibleCastleMoves = whiteKing.getCastleMoves(Color.BLACK, 0, defaultWhiteKingCoordinates,
                rookCoordinates, board);

        assertEquals(1, possibleCastleMoves.size());
    }

    @Test
    void testGetCastleMovesForBlack_leftSide() {
        final Board board = new Board();
        Coordinates defaultBlackKingCoordinates = new Coordinates(4,7);
        King blackKing = new King(defaultBlackKingCoordinates, Color.BLACK);

        board.move(new Move(new Coordinates(6,0), new Coordinates(7,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(1,7), new Coordinates(0,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(6,1), new Coordinates(6,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(1,6), new Coordinates(1,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,0), new Coordinates(6,1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(2,7), new Coordinates(1,6), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,1), new Coordinates(5,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(3,6), new Coordinates(3,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,2), new Coordinates(5,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(3,7), new Coordinates(3,6), MoveType.ORDINARY, SwitchPieceType.NULL));

        List<Coordinates> rookCoordinates = new ArrayList<>();
        rookCoordinates.add(new Coordinates(0, 7));
        rookCoordinates.add(new Coordinates(7, 7));

        assertTrue(board.getPiece(defaultBlackKingCoordinates) instanceof King);
        assertSame(board.getPiece(defaultBlackKingCoordinates).getColor(), Color.BLACK);

        List<Coordinates> possibleCastleMoves = blackKing.getCastleMoves(Color.WHITE, 7, defaultBlackKingCoordinates,
                rookCoordinates, board);

        assertEquals(1, possibleCastleMoves.size());
    }

    @Test
    void testGetCastleMovesForBlack_rightSide() {
        final Board board = new Board();
        Coordinates defaultBlackKingCoordinates = new Coordinates(4,7);
        King blackKing = new King(defaultBlackKingCoordinates, Color.BLACK);

        board.move(new Move(new Coordinates(6,0), new Coordinates(7,2), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6,7), new Coordinates(7,5), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(6,1), new Coordinates(6,3), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(6,6), new Coordinates(6,4), MoveType.ORDINARY, SwitchPieceType.NULL));

        board.move(new Move(new Coordinates(5,0), new Coordinates(6,1), MoveType.ORDINARY, SwitchPieceType.NULL));
        board.move(new Move(new Coordinates(5,7), new Coordinates(6,6), MoveType.ORDINARY, SwitchPieceType.NULL));

        List<Coordinates> rookCoordinates = new ArrayList<>();
        rookCoordinates.add(new Coordinates(0, 7));
        rookCoordinates.add(new Coordinates(7, 7));

        assertTrue(board.getPiece(defaultBlackKingCoordinates) instanceof King);
        assertSame(board.getPiece(defaultBlackKingCoordinates).getColor(), Color.BLACK);

        List<Coordinates> possibleCastleMoves = blackKing.getCastleMoves(Color.WHITE, 7, defaultBlackKingCoordinates,
                rookCoordinates, board);

        assertEquals(1, possibleCastleMoves.size());
    }
}