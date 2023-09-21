package io.deeplay.communication.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.ErrorResponseDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;

public class DeserializationService {
    private static final Gson gson = new Gson();

    /**
     * Десериализует строку в выбранный класс
     *
     * @param json  строка в json формате
     * @param clazz класс, в который мы хотим превратить строку
     * @param <T>   нужен для возврата любого класса
     * @return десериализованный объект
     */
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

    /**
     * Метод convertJsonToErrorResponseDTO преобразует JSON-строку в объект типа ErrorResponseDTO.
     *
     * @param json JSON-строка, содержащая данные об ошибке
     * @return объект типа ErrorResponseDTO, соответствующий переданной JSON-строке
     */
    public static ErrorResponseDTO convertJsonToErrorResponseDTO(String json) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.fromJson(json, ErrorResponseDTO.class);
    }
}
