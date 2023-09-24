package io.deeplay.communication.service;

import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.ErrorResponseDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.Color;
import io.deeplay.communication.model.GameStateType;
import io.deeplay.communication.model.GameType;
import io.deeplay.domain.BotType;
import io.deeplay.exception.GameLogicException;
import io.deeplay.model.move.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeserializationServiceTest {

    @Test
    void convertJsonToMoveDTO() {
        String json = "{\"startPosition\":{\"x\":0,\"y\":1},\"endPosition\":{\"x\":0,\"y\":3},\"moveType\":\"ORDINARY\",\"switchPieceType\":\"NULL\"}";
        final MoveDTO moveDTO = DeserializationService.convertJsonToMoveDTO(json);
        final Move expectedMove = new Move(
                new io.deeplay.model.Coordinates(0,1), new io.deeplay.model.Coordinates(0, 3),
                io.deeplay.domain.MoveType.ORDINARY, io.deeplay.domain.SwitchPieceType.NULL);

        assertEquals(expectedMove.startPosition().getX(), moveDTO.getStartPosition().getX());
        assertEquals(expectedMove.startPosition().getY(), moveDTO.getStartPosition().getY());
        assertEquals(expectedMove.endPosition().getX(), moveDTO.getEndPosition().getX());
        assertEquals(expectedMove.endPosition().getY(), moveDTO.getEndPosition().getY());
        assertEquals(expectedMove.moveType().toString(), moveDTO.getMoveType().toString());
        assertEquals(expectedMove.switchPieceType().toString(), moveDTO.getSwitchPieceType().toString());
    }

    @Test
    void convertJsonToStartGameDTO() {
        String json = "{\"gameType\":\"BotVsBot\",\"currentColor\":\"WHITE\",\"botType\":\"NEGAMAX_MARINA\"}";
        final StartGameDTO startGameDTO = DeserializationService.convertJsonToStartGameDTO(json);
        final StartGameDTO expectedStartGameDTO =
                new StartGameDTO(GameType.BotVsBot, Color.WHITE, BotType.NEGAMAX_MARINA);

        assertEquals(expectedStartGameDTO.getGameType(), startGameDTO.getGameType());
        assertEquals(expectedStartGameDTO.getCurrentColor(), startGameDTO.getCurrentColor());
        assertEquals(expectedStartGameDTO.getBotType(), startGameDTO.getBotType());
    }

    @Test
    void convertJsonToEndGameDTO() {
        String json1 = "{\"endGameStateType\":\"SURRENDER\",\"winColor\":\"BLACK\"}";
        EndGameDTO expectedEndGameDTO1 = new EndGameDTO(GameStateType.SURRENDER, Color.BLACK);
        EndGameDTO actualEndGameDTO1 = DeserializationService.convertJsonToEndGameDTO(json1);

        String json2 = "{\"endGameStateType\":\"DRAW\"}";
        EndGameDTO expectedEndGameDTO2 = new EndGameDTO(GameStateType.DRAW, null);
        EndGameDTO actualEndGameDTO2 = DeserializationService.convertJsonToEndGameDTO(json2);

        assertEquals(expectedEndGameDTO1.getEndGameStateType(), actualEndGameDTO1.getEndGameStateType());
        assertEquals(expectedEndGameDTO1.getWinColor(), actualEndGameDTO1.getWinColor());
        assertEquals(expectedEndGameDTO2.getEndGameStateType(), actualEndGameDTO2.getEndGameStateType());
        assertEquals(expectedEndGameDTO2.getWinColor(), actualEndGameDTO2.getWinColor());
    }

    @Test
    void convertJsonToErrorResponseDTO() {
        String json = "{\"exception\":{},\"message\":\"White King is not on the board!\"}";
        final ErrorResponseDTO actualErrorResponseDTO = DeserializationService.convertJsonToErrorResponseDTO(json);
        final ErrorResponseDTO expectedErrorResponseDTO = new ErrorResponseDTO(
                new GameLogicException(), "White King is not on the board!");

        assertNotNull(actualErrorResponseDTO.getException());
        assertEquals(expectedErrorResponseDTO.getMessage(), actualErrorResponseDTO.getMessage());
    }
}