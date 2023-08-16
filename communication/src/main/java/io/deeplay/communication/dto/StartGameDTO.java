package io.deeplay.communication.dto;

import io.deeplay.communication.model.Color;
import io.deeplay.communication.model.GameType;
import lombok.Getter;

@Getter
public class StartGameDTO {
    private GameType gameType;
    private final Color currentColor;

    public StartGameDTO(GameType gameType) {
        this.gameType = gameType;
        this.currentColor = Color.WHITE;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
}
