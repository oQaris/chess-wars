package io.deeplay.domain;

public enum MoveType {
    ORDINARY,
    TAKE,
    EN_PASSANT,
    CASTLING,
    PROMOTION;

    /**
     * Method return the name of enum field name.
     *
     * @return enum field name
     */
    public String getMoveType() {
        return this.name();
    }
}