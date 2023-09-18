package io.deeplay.communication.dto;

import io.deeplay.communication.model.Color;
import io.deeplay.communication.model.GameType;
import io.deeplay.domain.BotType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartGameDTO {
    private GameType gameType;
    private Color currentColor;
    private BotType botType;

    public StartGameDTO(GameType gameType, Color color, BotType botType) {
        this.gameType = gameType;
        this.currentColor = color;
        this.botType = botType;
    }

    @Override
    public String toString() {
        return "StartGameDTO{" +
                "gameType=" + gameType +
                ", currentColor=" + currentColor +
                ", botType=" + botType +
                '}';
    }
}
