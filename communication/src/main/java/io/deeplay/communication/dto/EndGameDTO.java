package io.deeplay.communication.dto;

import io.deeplay.communication.model.Color;
import io.deeplay.communication.model.GameStateType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndGameDTO {
    private GameStateType endGameStateType;
    private Color winColor;
}
