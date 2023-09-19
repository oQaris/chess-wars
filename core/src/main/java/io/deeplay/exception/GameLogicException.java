package io.deeplay.exception;

public class GameLogicException extends RuntimeException {
    public GameLogicException() {
        super();
    }

    /**
     * Конструктор с сообщением об ошибке, которое передается в родительский класс
     * @param errorMessage сообщение об ошибке
     */
    public GameLogicException(String errorMessage) {
        super(errorMessage);
    }
}
