package io.deeplay.communication.dto;

import io.deeplay.communication.model.Color;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoveTransferDTO {
    private Color currentMoveColor;
}
