package io.deeplay.communication.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.deeplay.communication.dto.*;

public class DeserializationService {
    private static final Gson gson = new Gson();

    private static <T> T deserialize(final String json, final Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("Something went wrong with deserialization of json - " + json + e);
        }
    }

    public static MoveDTO convertJsonToMoveDTO(final String json) {
        return deserialize(json, MoveDTO.class);
    }

    public static StartGameDTO convertJsonToStartGameDTO(final String json) {
        return deserialize(json, StartGameDTO.class);
    }

    public static EndGameDTO convertJsonToEndGameDTO(final String json) {
        return deserialize(json, EndGameDTO.class);
    }

    public static MoveTransferDTO convertJsonToMoveTransferDTO(final String json) {
        return deserialize(json, MoveTransferDTO.class);
    }

    public static ErrorResponseDTO convertJsonToErrorResponseDTO(final String json) {
        return deserialize(json, ErrorResponseDTO.class);
    }
}
