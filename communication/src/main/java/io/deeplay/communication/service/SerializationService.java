package io.deeplay.communication.service;

import com.google.gson.Gson;
import io.deeplay.communication.dto.MoveDTO;

public class SerializationService {
    private static final Gson gson = new Gson();

    private static String serialize(final Object obj) {
        return gson.toJson(obj);
    }

    public static String makeMoveDTOToJson(final MoveDTO moveDTO) {
        return serialize(moveDTO);
    }
}