package io.deeplay.model.piece;

import io.deeplay.domain.Color;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Coordinates coordinates, Color color) {
        super(coordinates, color);
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    /**
     * Метод для получения возможных ходов фигуры Pawn (пешка) на доске.
     *
     * @param board доска, на которой находится фигура
     * @return список возможных ходов фигуры
     */
    @Override
    public List<Coordinates> getPossibleMoves(Board board) {
        List<Coordinates> possibleMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canMoveAt(new Coordinates(i, j), board)) {
                    possibleMoves.add(new Coordinates(i, j));
                }
            }
        }

        return possibleMoves;
    }

    /**
     * Проверяет, может ли данная фигура Pawn (пешка) сделать ход на заданные координаты на заданной доске.
     * @param coordinates координаты для проверки возможности хода
     * @param board доска для проверки возможности хода
     * @return true, если ход возможен, false в противном случае
     */
    public boolean canMoveAt(Coordinates coordinates, Board board) {
        int currentX = getCoordinates().getX();
        int currentY = getCoordinates().getY();
        int targetX = coordinates.getX();
        int targetY = coordinates.getY();

        if (targetX < 0 || targetY < 0 || targetY >= 8 || targetX >= 8) {
            return false;
        }

        if ((currentX == targetX) && (currentY == targetY)) {
            return false;
        }

        if (board.getBoard()[targetX][targetY].getColor().equals(getColor())) { // фигура того же цвета
            return false;
        }

        if (Math.abs(targetY - currentY) > 2 || Math.abs(targetX - currentX) > 1) {
            return false;
        }

        if ((getColor() == Color.WHITE) && targetY <= currentY) { // только вперед хотьба
            return false;
        }

        if ((getColor() == Color.BLACK) && targetY >= currentY) {
            return false;
        }

        if (isEnPassant(coordinates, board)) { // взятие на проходе
            return true;
        }

        if (Math.abs(targetY - currentY) == 2 && isStartPosition() && currentX == targetX) { // только из стартовой позиции
            if (board.getBoard()[targetX][targetY].getColor().equals(Color.EMPTY)) {
                return board.getBoard()[currentX][currentY + 1].getColor().equals(Color.EMPTY)
                        || board.getBoard()[currentX][currentY - 1].getColor().equals(Color.EMPTY);
            }
        }

        if (targetX == currentX && Math.abs(targetY - currentY) == 1
                && board.getBoard()[targetX][targetY].getColor().equals(Color.EMPTY)) {
            return true;
        }

        if (Math.abs(targetY - currentY) == 1 && Math.abs(targetX - currentX) == 1) {
            return !board.getBoard()[targetX][targetY].getColor().equals(Color.EMPTY);
        }

        return false;
    }

    private boolean isStartPosition() {
        return (getColor() == Color.WHITE && getCoordinates().getY() == 1)
                || (getColor() == Color.BLACK && getCoordinates().getY() == 6);
    }

    public boolean isPromotion(Coordinates coordinates, Board board) {
        if ((coordinates.getY() == 0 && getColor().equals(Color.BLACK))
                || (coordinates.getY() == 7 && getColor().equals(Color.WHITE))) {
            return this.canMoveAt(coordinates, board);
        }

        return false;
    }

    public boolean isEnPassant(Coordinates coordinates, Board board) {
        int currentX = this.getCoordinates().getX();
        int currentY = this.getCoordinates().getY();

        if (!(getColor().equals(Color.WHITE) && currentY == 4)
                && !(getColor().equals(Color.BLACK) && currentY == 3)) { // стартовая позиция верная
            return false;
        }

        int targetX = coordinates.getX();
        int targetY = coordinates.getY();

        if (!(Math.abs(targetY - currentY) == 1 && Math.abs(targetX - currentX) == 1)) { // движение по диагонали
            return false;
        }

        if (!board.getBoard()[targetX][targetY].getColor().equals(Color.EMPTY)) { // целевая клетка пустая
            return false;
        }

        Move lastMove = board.getMoveHistory().getLastMove();

        Piece pieceMadeLastMove = board.getPiece(new Coordinates(lastMove.endPosition().getX(),
                lastMove.endPosition().getY()));

        if (!(pieceMadeLastMove instanceof Pawn)) {
            return false;
        }

        return (lastMove.startPosition().getX() == lastMove.endPosition().getX())
                && (Math.abs(lastMove.endPosition().getY() - lastMove.startPosition().getY()) == 2);
    }

    @Override
    public String toString() {
        return "Pawn: x = " + getCoordinates().getX() + ", y = " + getCoordinates().getY();
    }
}
