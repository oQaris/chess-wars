package io.deeplay.model.player;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Piece;
import io.deeplay.service.MoveService;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

public class Human extends Player {
    public Human(Color color) {
        super(color);
    }

    @Override
    public Move move(List<Piece> possiblePiecesToMove, Board board) {
        Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());

        Piece selectedPiece = null;
        while (selectedPiece == null) {
            System.out.println("choose piece (its number) which you want to move:");
            for (int i = 0; i < possiblePiecesToMove.size(); i++) {
                System.out.println("(" + i + ") " + possiblePiecesToMove.get(i).getColor().name() + " "
                        + possiblePiecesToMove.get(i).getClass().getName() + " at x:" + possiblePiecesToMove.get(i).getCoordinates().getX()
                        + " y:" + possiblePiecesToMove.get(i).getCoordinates().getY());
            }

            selectedPiece = possiblePiecesToMove.get(scanner.nextInt());
        }
        List<Coordinates> availableMoves = selectedPiece.getPossibleMoves(board);

        Coordinates moveCoordinates = null;

        while (moveCoordinates == null) {
            System.out.println("choose coordinates in which you want to move you piece:");
            for (int i = 0; i < availableMoves.size(); i++) {
                System.out.println("(" + i + ") x: " + availableMoves.get(i).getX() + " y: " + availableMoves.get(i).getY());
            }

            moveCoordinates = availableMoves.get(scanner.nextInt());
        }

        System.out.println("You selected " + selectedPiece.getColor().name() + " "
                + selectedPiece.getClass().getName() + " to move to coordinates x:" + moveCoordinates.getX() + " y:" + moveCoordinates.getY());

        return MoveService.createMove(selectedPiece, moveCoordinates, board);
    }

    public void lose() {
        // surrender
    }
}
