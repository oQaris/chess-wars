package gui.model;

public enum PieceColorIcon {
    BLACK_KING("/img/black_king.png"),
    BLACK_QUEEN("/img/black_queen.png"),
    BLACK_ROOK("/img/black_rook.png"),
    BLACK_KNIGHT("/img/black_knight.png"),
    BLACK_BISHOP("/img/black_bishop.png"),
    BLACK_PAWN("/img/black_pawn.png"),
    WHITE_KING("/img/white_king.png"),
    WHITE_QUEEN("/img/white_queen.png"),
    WHITE_ROOK("/img/white_rook.png"),
    WHITE_KNIGHT("/img/white_knight.png"),
    WHITE_BISHOP("/img/white_bishop.png"),
    WHITE_PAWN("/img/white_pawn.png");

    /** Путь к файлу, лежащий в поле enum **/
    public final String path;
    private PieceColorIcon(String path) {
        this.path = path;
    }
}
