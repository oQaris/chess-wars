package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.service.IUserCommunication;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@Getter
public class Bot extends Player {
    private final int difficultyLevel;

    public Bot(Color color, int difficultyLevel, IUserCommunication iUserCommunication) {
        super(color, iUserCommunication);
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * Возвращает созданный объект класса Move с заданными параметрами. Метод рандомно выбирает параметры из листов
     * possiblePiecesToMove и availableMoves. Если данный ход является promotion - выбирает рандомно фигуру,
     * в которую пешка может превратиться.
     * @param board текущее состояние доски
     * @param currentColor цвет текущего хода
     * @return новый объект класса Move
     */
    @Override
    public Move getMove(Board board, Color currentColor) {
        Random random = new Random();

        List<Piece> possiblePiecesToMove = getPiecesPossibleToMove(board, currentColor);

        Piece randomPiece = null;
        if (possiblePiecesToMove.size() == 1) randomPiece = possiblePiecesToMove.get(0);
        else randomPiece = possiblePiecesToMove.get(random.nextInt(possiblePiecesToMove.size() - 1));

        List<Coordinates> availableMoves = randomPiece.getPossibleMoves(board);
        List<Coordinates> movesWithoutCheck = GameState.getMovesWithoutMakingCheck(board, randomPiece, availableMoves);
        availableMoves.retainAll(movesWithoutCheck);

//        System.out.println("Number of moves you can do using this Piece: " + availableMoves.size());

        Coordinates randomMoveCoordinates = null;
        if (availableMoves.size() == 1) randomMoveCoordinates = availableMoves.get(0);
        else randomMoveCoordinates = availableMoves.get(random.nextInt(availableMoves.size() - 1));

        MoveType moveType = getType(randomPiece, randomMoveCoordinates, board);
        SwitchPieceType selectedSwitchPiece = SwitchPieceType.NULL;

        if (moveType == MoveType.PROMOTION) {
            switch (SwitchPieceType.getRandomPiece()) {
                case BISHOP -> selectedSwitchPiece = SwitchPieceType.BISHOP;
                case KNIGHT -> selectedSwitchPiece = SwitchPieceType.KNIGHT;
                case QUEEN -> selectedSwitchPiece = SwitchPieceType.QUEEN;
                case ROOK -> selectedSwitchPiece = SwitchPieceType.ROOK;
            }
        }

//        System.out.println("Bot selected to move: " + randomPiece.getColor() + " " + randomPiece.getClass().getSimpleName()
//                + " to coordinates: x=" + randomMoveCoordinates.getX() + ", y=" + randomMoveCoordinates.getY());

        return new Move(randomPiece.getCoordinates(), randomMoveCoordinates, moveType, selectedSwitchPiece);
    }

    @Override
    public String toString() {
        return "Bot{" +
                "difficultyLevel=" + difficultyLevel +
                ", color=" + color +
                '}';
    }
}