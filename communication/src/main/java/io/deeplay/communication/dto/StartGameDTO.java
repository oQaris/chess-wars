package io.deeplay.communication.dto;

import io.deeplay.communication.model.Color;
import io.deeplay.communication.model.GameType;
import lombok.Getter;

@Getter
public class StartGameDTO {
    private GameType gameType;
    private final Color currentColor;
    private int botLevel;

    public StartGameDTO(GameType gameType, Color color, int botLevel) {
        this.gameType = gameType;
        this.currentColor = color;
        this.botLevel = botLevel;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public String toString() {
        return "StartGameDTO{" +
                "gameType=" + gameType +
                ", currentColor=" + currentColor +
                ", botLevel=" + botLevel +
                '}';
    }
}
