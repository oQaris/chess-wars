package io.deeplay.communication.service;

import com.google.gson.Gson;
import io.deeplay.communication.dto.*;

public class SerializationService {
    private static final Gson gson = new Gson();

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

    public static String convertMoveTransferDTOtoJSON(final MoveTransferDTO moveTransferDTO) {
        return serialize(moveTransferDTO);
    }

    public static String convertErrorResponseDTOtoJSON(final ErrorResponseDTO errorResponseDTO) {
        return serialize(errorResponseDTO);
    }
}