package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.service.UserCommunicationService;

import java.util.ArrayList;
import java.util.List;

import static io.deeplay.model.Board.BOARD_HEIGHT;
import static io.deeplay.model.Board.BOARD_LENGTH;

public class Human extends Player {
    public Human(Color color) {
        super(color);
    }

    /**
     * Возвращает созданный объект класса Move с заданными параметрами. Получает параметры из ввода пользователя
     *
     * @param board текущее состояние доски
     * @param currentColor цвет текущего хода
     * @return новый объект класса Move
     */
    @Override
    public Move getMove(Board board, Color currentColor) {
        UserCommunicationService userCommunicationService = new UserCommunicationService(System.in, System.out);
        List<Piece> possiblePiecesToMove = getPiecesPossibleToMove(board, currentColor);
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

    /**
     * Метод для запроса на проигрыш
     */
    public void lose() {
        // surrender
    }
}
