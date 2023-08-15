package io.deeplay.communication.dto;

import io.deeplay.communication.model.Coordinates;
import io.deeplay.communication.model.MoveType;
import io.deeplay.communication.model.SwitchPieceType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MoveDTO {
    private Coordinates startPosition;
    private Coordinates endPosition;
    private MoveType moveType;
    private SwitchPieceType switchPieceType;
}