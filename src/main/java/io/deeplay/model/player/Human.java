package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.service.MoveService;
import io.deeplay.service.UserCommunicationService;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

public class Human extends Player {
    public Human(Color color) {
        super(color);
    }

    @Override
    public Move getMove(List<Piece> possiblePiecesToMove, Board board) {
        Piece selectedPiece = UserCommunicationService.selectPiece(possiblePiecesToMove);
        List<Coordinates> availableMoves = selectedPiece.getPossibleMoves(board);

        Coordinates moveCoordinates = UserCommunicationService.selectCoordinates(availableMoves);

        System.out.println("You selected " + selectedPiece.getColor().name() + " "
                + selectedPiece.getClass().getSimpleName() + " to move to coordinates x:" + moveCoordinates.getX()
                + " y:" + moveCoordinates.getY());

        return MoveService.createMove(selectedPiece, moveCoordinates, board);
    }

    public void lose() {
        // surrender
    }
}
