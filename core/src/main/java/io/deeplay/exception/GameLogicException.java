package io.deeplay.exception;

public class GameLogicException extends RuntimeException {
    public GameLogicException() {
        super();
    }
    public GameLogicException(String errorMessage) {
        super(errorMessage);
    }
}
