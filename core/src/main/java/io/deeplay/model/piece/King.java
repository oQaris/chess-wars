package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.service.KingService;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    // 1.1) Проверка на то, что король не ходил
    // 1.2) Проверка на то, что ладья не ходила
    // 2) Проверка на возможно стоящие фигуры между ладьей и королем *
    // 3) Проверка на то, что король не под шахом *
    // 4) Проверка на то, что король не будет под шахом на полях слева или справа *
    // 5) Проверка на то, что король не будет под шахом на поле, на которое он встанет, слева или справа *
    @Override
    public List<Coordinates> getPossibleMoves(Board board) {
        List<Coordinates> possibleMoves = new ArrayList<>();

        List<Coordinates> rookCoordinates = new ArrayList<>();
        Coordinates[] defaultKings = {new Coordinates(4,0), new Coordinates(4, 7)};
        Coordinates[] defaultBottomRooks = {new Coordinates(0,0), new Coordinates(7, 0)};
        Coordinates[] defaultUpRooks = {new Coordinates(0,7), new Coordinates(7, 7)};

        int x = this.getCoordinates().getX();
        int y = this.getCoordinates().getY();

        if (this.getColor() == Color.WHITE) {
            if (!board.getPieceMoved()[defaultKings[0].getX()][defaultKings[0].getY()]) { // двигался ли король
                for (Coordinates rookCoordinate : defaultBottomRooks) { // проход по башням
                    if (!board.getPieceMoved()[rookCoordinate.getX()][rookCoordinate.getY()])
                        rookCoordinates.add(rookCoordinate);
                }
                if (!rookCoordinates.isEmpty())
                    possibleMoves.addAll(getWhiteCastleMoves(new Coordinates(x, y), rookCoordinates, board));
            }
        } else {
            if (!board.getPieceMoved()[defaultKings[1].getX()][defaultKings[1].getY()]) {
                for (Coordinates rookCoordinate : defaultUpRooks) {
                    if (!board.getPieceMoved()[rookCoordinate.getX()][rookCoordinate.getY()])
                        rookCoordinates.add(rookCoordinate);
                }
                if (!rookCoordinates.isEmpty())
                    possibleMoves.addAll(getBlackCastleMoves(new Coordinates(x, y), rookCoordinates, board));
            }
        }

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && !(i == x && j == y)) {
                    Coordinates coordinates = new Coordinates(i, j);
                    if (canMoveAt(coordinates, board)) {
                        possibleMoves.add(coordinates);
                    }
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        if (coordinates.getX() < 0 || coordinates.getY() < 0 || coordinates.getY() >= 8 || coordinates.getX() >= 8) {
            return false;
        }

        if (this.getCoordinates().getX() == coordinates.getX() && this.getCoordinates().getY() == coordinates.getY()) {
            return false;
        }

        if (board.getBoard()[coordinates.getX()][coordinates.getY()].getColor().equals(getColor())) { // фигура того же цвета
            return false;
        }

        int distanceX = Math.abs(coordinates.getX() - this.getCoordinates().getX());
        int distanceY = Math.abs(coordinates.getY() - this.getCoordinates().getY());

        return distanceX <= 1 && distanceY <= 1;
    }

    List<Coordinates> getWhiteCastleMoves(Coordinates kingCoordinates, List<Coordinates> rookCoordinates, Board board) {
        List<Coordinates> checkedAvailableCoordinates = new ArrayList<>();

        for (Coordinates coordinates : rookCoordinates) {
            if (coordinates.getX() == 0 && coordinates.getY() == 0) {
                List<Coordinates> tempCoordinates = new ArrayList<>();

                if (board.getPiece(new Coordinates(1, 0)) instanceof Empty &&
                        board.getPiece(new Coordinates(2, 0)) instanceof Empty &&
                        board.getPiece(new Coordinates(3, 0)) instanceof Empty) {
                    tempCoordinates.add(new Coordinates(2, 0));
                    tempCoordinates.add(new Coordinates(3, 0));

                    if (KingService.isPossibleToCastle(kingCoordinates,
                            tempCoordinates, board, Color.BLACK)) {
                        checkedAvailableCoordinates.add(new Coordinates(2,0));
                    }
                }
            }

            if (coordinates.getX() == 7 && coordinates.getY() == 0) {
                List<Coordinates> tempCoordinates = new ArrayList<>();

                if (board.getPiece(new Coordinates(5, 0)) instanceof Empty &&
                        board.getPiece(new Coordinates(6, 0)) instanceof Empty) {
                    tempCoordinates.add(new Coordinates(5, 0));
                    tempCoordinates.add(new Coordinates(6, 0));

                    if (KingService.isPossibleToCastle(kingCoordinates,
                            tempCoordinates, board, Color.BLACK)) {
                        checkedAvailableCoordinates.add(new Coordinates(6,0));
                    }
                }
            }
        }

        return checkedAvailableCoordinates;
    }

    List<Coordinates> getBlackCastleMoves(Coordinates kingCoordinates, List<Coordinates> rookCoordinates, Board board) {
        List<Coordinates> checkedAvailableCoordinates = new ArrayList<>();

        for (Coordinates coordinates : rookCoordinates) {
            if (coordinates.getX() == 0 && coordinates.getY() == 7) {
                List<Coordinates> tempCoordinates = new ArrayList<>();

                if (board.getPiece(new Coordinates(1, 7)) instanceof Empty &&
                        board.getPiece(new Coordinates(2, 7)) instanceof Empty &&
                        board.getPiece(new Coordinates(3, 7)) instanceof Empty) {
                    tempCoordinates.add(new Coordinates(2, 7));
                    tempCoordinates.add(new Coordinates(3, 7));

                    if (KingService.isPossibleToCastle(kingCoordinates,
                            tempCoordinates, board, Color.WHITE)) {
                        checkedAvailableCoordinates.add(new Coordinates(2,7));
                    }
                }
            }

            if (coordinates.getX() == 7 && coordinates.getY() == 7) {
                List<Coordinates> tempCoordinates = new ArrayList<>();

                if (board.getPiece(new Coordinates(5, 7)) instanceof Empty &&
                        board.getPiece(new Coordinates(6, 7)) instanceof Empty) {
                    tempCoordinates.add(new Coordinates(5, 7));
                    tempCoordinates.add(new Coordinates(6, 7));

                    if (KingService.isPossibleToCastle(kingCoordinates,
                            tempCoordinates, board, Color.WHITE)) {
                        checkedAvailableCoordinates.add(new Coordinates(6,7));
                    }
                }
            }
        }

        return checkedAvailableCoordinates;
    }

    @Override
    public String toString() {
        return "King: x = " + getCoordinates().getX() + " y = " + getCoordinates().getY();
    }
}
