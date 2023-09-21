package io.deeplay.communication.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.ErrorResponseDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;

public class SerializationService {
    private static final Gson gson = new Gson();

    /**
     * Сериализует объект в json формат
     *
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

    /**
     * Метод преобразует объект типа ErrorResponseDTO в формат JSON.
     *
     * @param errorResponseDTO объект типа ErrorResponseDTO, который нужно преобразовать
     * @return строка в формате JSON, представляющая переданный объект ErrorResponseDTO
     */
    public static String convertErrorResponseDTOtoJSON(ErrorResponseDTO errorResponseDTO) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(errorResponseDTO);
    }
}