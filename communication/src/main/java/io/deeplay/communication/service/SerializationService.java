package io.deeplay.communication.service;

import com.google.gson.Gson;
import io.deeplay.communication.dto.*;

public class SerializationService {
    private static final Gson gson = new Gson();

    /**
     * Сериализует объект в json формат
     * @param obj объект на сериализацию
     * @return строка в виде json
     */
    private static String serialize(final Object obj) {
        return gson.toJson(obj);
    }

    public static String convertMoveDTOToJson(final MoveDTO moveDTO) {
        return serialize(moveDTO);
    }

    public static String convertStartGameDTOtoJSON(final StartGameDTO startGameDTO) {
        return serialize(startGameDTO);
    }

    public static String convertEndGameDTOtoJSON(final EndGameDTO endGameDTO) {
        return serialize(endGameDTO);
    }
}