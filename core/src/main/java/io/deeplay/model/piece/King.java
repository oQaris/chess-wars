package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;

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

        int x = this.getCoordinates().getX();
        int y = this.getCoordinates().getY();

        Coordinates defaultKing = null;
        Coordinates[] defaultRooks = null;
        Color enemyColor;
        if (this.getColor() == Color.WHITE) {
            defaultKing = new Coordinates(4,0);
            defaultRooks = new Coordinates[]{new Coordinates(0, 0), new Coordinates(7, 0)};
            enemyColor = Color.BLACK;
        } else {
            defaultKing = new Coordinates(4, 7);
            defaultRooks = new Coordinates[]{new Coordinates(0, 7), new Coordinates(7, 7)};
            enemyColor = Color.WHITE;
        }

        if (!board.getPieceMoved()[defaultKing.getX()][defaultKing.getY()]) { // двигался ли король
            for (Coordinates rookCoordinate : defaultRooks) { // проход по башням
                if (!board.getPieceMoved()[rookCoordinate.getX()][rookCoordinate.getY()])
                    rookCoordinates.add(rookCoordinate);
            }
            if (!rookCoordinates.isEmpty())
                possibleMoves.addAll(getCastleMoves(enemyColor, defaultKing.getY(),
                        new Coordinates(x, y), rookCoordinates, board));
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

    List<Coordinates> getCastleMoves(Color enemyColor, int y_coordinate, Coordinates kingCoordinates, List<Coordinates> rookCoordinates, Board board) {
        List<Coordinates> checkedAvailableCoordinates = new ArrayList<>();

        for (Coordinates coordinates : rookCoordinates) {
            if (coordinates.getX() == 0 && coordinates.getY() == y_coordinate) {
                List<Coordinates> tempCoordinates = new ArrayList<>();

                if (board.getPiece(new Coordinates(1, y_coordinate)) instanceof Empty &&
                        board.getPiece(new Coordinates(2, y_coordinate)) instanceof Empty &&
                        board.getPiece(new Coordinates(3, y_coordinate)) instanceof Empty) {
                    tempCoordinates.add(new Coordinates(2, y_coordinate));
                    tempCoordinates.add(new Coordinates(3, y_coordinate));

                    if (isPossibleToCastle(kingCoordinates,
                            tempCoordinates, board, enemyColor)) {
                        checkedAvailableCoordinates.add(new Coordinates(2,y_coordinate));
                    }
                }
            }

            if (coordinates.getX() == 7 && coordinates.getY() == y_coordinate) {
                List<Coordinates> tempCoordinates = new ArrayList<>();

                if (board.getPiece(new Coordinates(5, y_coordinate)) instanceof Empty &&
                        board.getPiece(new Coordinates(6, y_coordinate)) instanceof Empty) {
                    tempCoordinates.add(new Coordinates(5, y_coordinate));
                    tempCoordinates.add(new Coordinates(6, y_coordinate));

                    if (isPossibleToCastle(kingCoordinates,
                            tempCoordinates, board, enemyColor)) {
                        checkedAvailableCoordinates.add(new Coordinates(6,y_coordinate));
                    }
                }
            }
        }

        return checkedAvailableCoordinates;
    }

    boolean isPossibleToCastle(Coordinates kingCoordinates, List<Coordinates> potentialCoordinates,
                                             Board board, Color enemyColor) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(new Coordinates(i,j)).getColor().equals(enemyColor)) {
                    Piece piece = board.getPiece(new Coordinates(i,j));

                    if (piece.canMoveAt(kingCoordinates, board)) return false;

                    for (Coordinates potentialCoordinate : potentialCoordinates) {
                        if (piece.canMoveAt(potentialCoordinate, board))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "King: x = " + getCoordinates().getX() + " y = " + getCoordinates().getY();
    }
}
