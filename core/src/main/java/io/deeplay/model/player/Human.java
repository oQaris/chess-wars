package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.service.UserCommunicationService;

import java.util.List;

public class Human extends Player {
    public Human(Color color) {
        super(color);
    }

    @Override
    public Move getMove(List<Piece> possiblePiecesToMove, Board board) {
        UserCommunicationService userCommunicationService = new UserCommunicationService(System.in, System.out);
        Piece selectedPiece = userCommunicationService.selectPiece(possiblePiecesToMove);
        List<Coordinates> availableMoves = selectedPiece.getPossibleMoves(board);
        Coordinates moveCoordinates = userCommunicationService.selectCoordinates(availableMoves);
        MoveType moveType = getType(selectedPiece, moveCoordinates, board);

        SwitchPieceType selectedSwitchPiece = SwitchPieceType.NULL;
        if (moveType == MoveType.PROMOTION) selectedSwitchPiece = userCommunicationService.selectSwitchPiece();

        System.out.println("You selected " + selectedPiece.getColor().name() + " "
                + selectedPiece.getClass().getSimpleName() + " to move to coordinates x:" + moveCoordinates.getX()
                + " y:" + moveCoordinates.getY());

        return new Move(selectedPiece.getCoordinates(), moveCoordinates, moveType, selectedSwitchPiece);
    }

    public void lose() {
        // surrender
    }
}
