package io.deeplay.communication.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.deeplay.communication.dto.MoveDTO;

public class DeserializationService {
    private static final Gson gson = new Gson();

    private static <T> T deserialize(final String json, final Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("Something went wrong with deserialization of json - " + json + e);
        }
    }

    public static MoveDTO makeJsonToMoveDTO(final String json) {
        return deserialize(json, MoveDTO.class);
    }
}
