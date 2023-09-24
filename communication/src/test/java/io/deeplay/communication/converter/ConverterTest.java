package io.deeplay.communication.converter;

import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.ErrorResponseDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.*;
import io.deeplay.domain.BotType;
import io.deeplay.domain.GameStates;
import io.deeplay.exception.GameLogicException;
import io.deeplay.marinaAI.bot.MiniMaxBot;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    void testGetMoveTypeFromDTO() {
        final MoveDTO moveDTO = new MoveDTO(new Coordinates(0,1), new Coordinates(0, 3),
                MoveType.ORDINARY, SwitchPieceType.NULL);
        io.deeplay.domain.MoveType moveType = Converter.getMoveTypeFromDTO(moveDTO);

        assertEquals(io.deeplay.domain.MoveType.ORDINARY.toString(), moveType.toString());
        assertNotEquals(io.deeplay.domain.MoveType.PROMOTION.toString(), moveType.toString());
    }

    @Test
    void testGetMoveTypeFromMove() {
        final Move move = new Move(
                new io.deeplay.model.Coordinates(0,1), new io.deeplay.model.Coordinates(0, 3),
                io.deeplay.domain.MoveType.ORDINARY, io.deeplay.domain.SwitchPieceType.NULL);

        MoveType moveType = Converter.getMoveTypeFromMove(move);

        assertEquals(MoveType.ORDINARY.toString(), moveType.toString());
        assertNotEquals(MoveType.PROMOTION.toString(), moveType.toString());
    }

    @Test
    void testGetSwitchPieceType() {
        final MoveDTO moveDTO = new MoveDTO(new Coordinates(1,6), new Coordinates(2, 7),
                MoveType.ORDINARY, SwitchPieceType.QUEEN);
        io.deeplay.domain.SwitchPieceType switchPieceType = Converter.getSwitchPieceType(moveDTO);

        assertEquals(io.deeplay.domain.SwitchPieceType.QUEEN.toString(), switchPieceType.toString());
    }

    @Test
    void testGetStartGameSettings() {
        final List<String> gameSettings = new ArrayList<>(Arrays.asList("Бот vs. Бот", "Противник", "ExpectimaxIgor"));
        StartGameDTO startGameDTO = Converter.getStartGameSettings(gameSettings);
        StartGameDTO expectedStartGameDTO = new StartGameDTO(GameType.BotVsBot, Color.BLACK, BotType.EXPECTIMAX_IGOR);

        assertEquals(expectedStartGameDTO.getGameType(), startGameDTO.getGameType());
        assertEquals(expectedStartGameDTO.getBotType(), startGameDTO.getBotType());
        assertEquals(expectedStartGameDTO.getCurrentColor(), startGameDTO.getCurrentColor());
    }

    @Test
    void testCreateNewBot() {
        final Bot bot = Converter.createNewBot(BotType.MINIMAX_MARINA, io.deeplay.domain.Color.BLACK);

        assertEquals(Color.BLACK.toString(), bot.getColor().toString());
        assertTrue(bot instanceof MiniMaxBot);
    }

    @Test
    void testGetSwitchPieceTypeDTO() {
        final Move move = new Move(
                new io.deeplay.model.Coordinates(1,6), new io.deeplay.model.Coordinates(2, 7),
                io.deeplay.domain.MoveType.ORDINARY, io.deeplay.domain.SwitchPieceType.BISHOP);
        SwitchPieceType switchPieceType = Converter.getSwitchPieceTypeDTO(move);

        assertEquals(SwitchPieceType.BISHOP.toString(), switchPieceType.toString());
    }

    @Test
    void testConvertColor() {
        final io.deeplay.domain.Color convertedBlackColor = Converter.convertColor(Color.BLACK);
        final io.deeplay.domain.Color convertedWhiteColor = Converter.convertColor(Color.WHITE);
        final io.deeplay.domain.Color convertedEmpty = Converter.convertColor(Color.EMPTY);

        assertEquals(Color.BLACK.toString(), convertedBlackColor.toString());
        assertEquals(Color.WHITE.toString(), convertedWhiteColor.toString());
        assertEquals(Color.EMPTY.toString(), convertedEmpty.toString());
    }

    @Test
    void testConvertColorFromString() {
        final io.deeplay.communication.model.Color convertedBlackColor = Converter.convertColorFromString("BLACK");
        final io.deeplay.communication.model.Color convertedWhiteColor = Converter.convertColorFromString("WHITE");
        final io.deeplay.communication.model.Color convertedEmpty = Converter.convertColorFromString("EMPTY");

        assertEquals(io.deeplay.communication.model.Color.BLACK.toString(), convertedBlackColor.toString());
        assertEquals(io.deeplay.communication.model.Color.WHITE.toString(), convertedWhiteColor.toString());
        assertEquals(io.deeplay.communication.model.Color.EMPTY.toString(), convertedEmpty.toString());
    }

    @Test
    void testConvertEndGameDTOToGameStates() {
        final EndGameDTO endGameDTO = new EndGameDTO(GameStateType.CHECKMATE, Color.BLACK);
        final GameStates gameStatesCheckmate = Converter.convertEndGameDTOToGameStates(endGameDTO);

        final EndGameDTO endGameDTO2 = new EndGameDTO(GameStateType.SURRENDER, Color.BLACK);
        final GameStates gameStatesSurrender = Converter.convertEndGameDTOToGameStates(endGameDTO2);

        assertEquals(GameStates.CHECKMATE.toString(), gameStatesCheckmate.toString());
        assertEquals(GameStates.SURRENDER.toString(), gameStatesSurrender.toString());
    }

    @Test
    void testConvertListEndGameToEndGameDTO() {
        final List<String> listEndGame = new ArrayList<>(Arrays.asList("SURRENDER", "WHITE"));
        final EndGameDTO endGameDTO = Converter.convertListEndGameToEndGameDTO(listEndGame);

        assertEquals(GameStateType.SURRENDER.toString(), endGameDTO.getEndGameStateType().toString());
    }

    @Test
    void testConvertGameTypeDTO() {
        final io.deeplay.domain.GameType gameType = Converter.convertGameTypeDTO(GameType.HumanVsBot);

        assertEquals(GameType.HumanVsBot.toString(), gameType.toString());
    }

    @Test
    void testConvertDTOToMove() {
        final MoveDTO moveDTO = new MoveDTO(new Coordinates(1,6), new Coordinates(2, 7),
                MoveType.ORDINARY, SwitchPieceType.QUEEN);
        final Move move = Converter.convertDTOToMove(moveDTO);

        assertEquals(moveDTO.getStartPosition().getX(), move.startPosition().getX());
        assertEquals(moveDTO.getStartPosition().getY(), move.startPosition().getY());
        assertEquals(moveDTO.getEndPosition().getX(), move.endPosition().getX());
        assertEquals(moveDTO.getEndPosition().getY(), move.endPosition().getY());
        assertEquals(moveDTO.getMoveType().toString(), move.moveType().toString());
        assertEquals(moveDTO.getSwitchPieceType().toString(), move.switchPieceType().toString());
    }

    @Test
    void testConvertMoveToMoveDTO() {
        final Move move = new Move(
                new io.deeplay.model.Coordinates(3,1), new io.deeplay.model.Coordinates(3, 3),
                io.deeplay.domain.MoveType.ORDINARY, io.deeplay.domain.SwitchPieceType.NULL);
        final MoveDTO moveDTO = Converter.convertMoveToMoveDTO(move);

        assertEquals(move.startPosition().getX(), moveDTO.getStartPosition().getX());
        assertEquals(move.startPosition().getY(), moveDTO.getStartPosition().getY());
        assertEquals(move.endPosition().getX(), moveDTO.getEndPosition().getX());
        assertEquals(move.endPosition().getY(), moveDTO.getEndPosition().getY());
        assertEquals(move.moveType().toString(), moveDTO.getMoveType().toString());
        assertEquals(move.switchPieceType().toString(), moveDTO.getSwitchPieceType().toString());
    }

    @Test
    void testConvertEndGameStateDTO() {
        final EndGameDTO endGameDTO = new EndGameDTO(GameStateType.STALEMATE, Color.BLACK);
        final List<String> result = Converter.convertEndGameStateDTO(endGameDTO);

        assertEquals("STALEMATE", result.get(0));
        assertEquals("BLACK", result.get(1));
    }

    @Test
    void testConvertErrorToErrorResponseDTO() {
        final ErrorResponseDTO errorResponseDTO = Converter.convertErrorToErrorResponseDTO(
                new GameLogicException(), "White King is not on the board!");

        assertEquals("White King is not on the board!", errorResponseDTO.getMessage());
        assertTrue(errorResponseDTO.getException() instanceof GameLogicException);
    }

    @Test
    void testConvertErrorResponseDTOToList() {
        final ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                new GameLogicException(), "White King is not on the board!");

        List<Object> errorList = Converter.convertErrorResponseDTOToList(errorResponseDTO);

        assertTrue(errorList.get(0) instanceof GameLogicException);
        assertEquals("White King is not on the board!", errorList.get(1));
    }
}