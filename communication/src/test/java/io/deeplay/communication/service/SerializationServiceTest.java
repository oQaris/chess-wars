package io.deeplay.communication.service;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.ErrorResponseDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.*;
import io.deeplay.domain.BotType;
import io.deeplay.exception.GameLogicException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializationServiceTest {

    @Test
    void testConvertMoveDTOToJson() {
        final MoveDTO moveDTO = new MoveDTO(new Coordinates(0,1), new Coordinates(0, 3),
                MoveType.ORDINARY, SwitchPieceType.NULL);
        String actualJson = SerializationService.convertMoveDTOToJson(moveDTO);
        String expectedJson = "{\"startPosition\":{\"x\":0,\"y\":1},\"endPosition\":{\"x\":0,\"y\":3},\"moveType\":\"ORDINARY\",\"switchPieceType\":\"NULL\"}";

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testConvertStartGameDTOtoJSON() {
        StartGameDTO startGameDTO = new StartGameDTO(GameType.BotVsBot, Color.WHITE, BotType.NEGAMAX_MARINA);
        String actualJson = SerializationService.convertStartGameDTOtoJSON(startGameDTO);
        String expectedJson = "{\"gameType\":\"BotVsBot\",\"currentColor\":\"WHITE\",\"botType\":\"NEGAMAX_MARINA\"}";

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testConvertEndGameDTOtoJSON() {
        EndGameDTO endGameDTO = new EndGameDTO(GameStateType.SURRENDER, Color.BLACK);
        String actualJson = SerializationService.convertEndGameDTOtoJSON(endGameDTO);
        String expectedJson = "{\"endGameStateType\":\"SURRENDER\",\"winColor\":\"BLACK\"}";

        EndGameDTO endGameDTO2 = new EndGameDTO(GameStateType.DRAW, null);
        String actualJson2 = SerializationService.convertEndGameDTOtoJSON(endGameDTO2);
        String expectedJson2 = "{\"endGameStateType\":\"DRAW\"}";

        assertEquals(expectedJson, actualJson);
        assertEquals(expectedJson2, actualJson2);
    }

    @Test
    void testConvertErrorResponseDTOtoJSON() {
        final ErrorResponseDTO errorResponseDTO = Converter.convertErrorToErrorResponseDTO(
                new GameLogicException(), "White King is not on the board!");
        String actualJson = SerializationService.convertErrorResponseDTOtoJSON(errorResponseDTO);
        String expectedJson = "{\"exception\":{},\"message\":\"White King is not on the board!\"}";

        assertEquals(expectedJson, actualJson);
    }
}