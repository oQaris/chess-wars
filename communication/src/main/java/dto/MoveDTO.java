package dto;

import com.google.gson.annotations.SerializedName;
import io.deeplay.model.move.Move;

public class MoveDTO {
    @SerializedName("move")
    private Move move;

    public MoveDTO(Move move) {
        this.move = move;
    }
}