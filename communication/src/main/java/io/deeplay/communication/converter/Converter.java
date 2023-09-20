package io.deeplay.communication.converter;

import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.ErrorResponseDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.GameStateType;
import io.deeplay.domain.*;
import io.deeplay.igorAI.ExpectimaxBot;
import io.deeplay.igorAI.MinimaxBot;
import io.deeplay.igorAI.NegamaxBot;
import io.deeplay.marinaAI.bot.ExpectiMaxBot;
import io.deeplay.marinaAI.bot.MiniMaxBot;
import io.deeplay.marinaAI.bot.NegaMaxBot;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.player.Bot;
import io.deeplay.service.GuiUserCommunicationService;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    private static final String ERROR_MESSAGE = "Illegal parameter converting";

    public static MoveType getMoveTypeFromDTO(MoveDTO moveDTO) {
        String moveType = moveDTO.getMoveType().toString();

        return switch (moveType) {
            case "EN_PASSANT" -> MoveType.EN_PASSANT;
            case "ORDINARY" -> MoveType.ORDINARY;
            case "CASTLING" -> MoveType.CASTLING;
            case "PROMOTION" -> MoveType.PROMOTION;
            case "TAKE" -> MoveType.TAKE;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static io.deeplay.communication.model.MoveType getMoveTypeFromMove(Move move) {
        String moveType = move.moveType().toString();

        return switch (moveType) {
            case "EN_PASSANT" -> io.deeplay.communication.model.MoveType.EN_PASSANT;
            case "ORDINARY" -> io.deeplay.communication.model.MoveType.ORDINARY;
            case "CASTLING" -> io.deeplay.communication.model.MoveType.CASTLING;
            case "PROMOTION" -> io.deeplay.communication.model.MoveType.PROMOTION;
            case "TAKE" -> io.deeplay.communication.model.MoveType.TAKE;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static SwitchPieceType getSwitchPieceType(MoveDTO moveDTO) {
        String switchPieceType = moveDTO.getSwitchPieceType().toString();

        return switch (switchPieceType) {
            case "ROOK" -> SwitchPieceType.ROOK;
            case "KNIGHT" -> SwitchPieceType.KNIGHT;
            case "QUEEN" -> SwitchPieceType.QUEEN;
            case "BISHOP" -> SwitchPieceType.BISHOP;
            case "NULL" -> SwitchPieceType.NULL;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static StartGameDTO getStartGameSettings(List<String> gameSettings) {
        io.deeplay.communication.model.GameType clientGameType;
        io.deeplay.communication.model.Color clientColor;
        BotType botType;

        switch (gameSettings.get(0)) {
            case "Человек vs. Человек" -> clientGameType = io.deeplay.communication.model.GameType.HumanVsHuman;
            case "Человек vs. Бот" -> clientGameType = io.deeplay.communication.model.GameType.HumanVsBot;
            case "Бот vs. Бот" -> clientGameType = io.deeplay.communication.model.GameType.BotVsBot;
            default -> throw new IllegalArgumentException("Wrong Game Type selection");
        }

        switch (gameSettings.get(1)) {
            case "Я" -> clientColor = io.deeplay.communication.model.Color.WHITE;
            case "Противник" -> clientColor = io.deeplay.communication.model.Color.BLACK;
            default -> throw new IllegalArgumentException("Wrong Color selection");
        }

        switch (gameSettings.get(2)) {
            case "Random" -> botType = BotType.RANDOM;
            case "MinimaxIgor" -> botType = BotType.MINIMAX_IGOR;
            case "MinimaxMarina" -> botType = BotType.MINIMAX_MARINA;
            case "NegamaxIgor" -> botType = BotType.NEGAMAX_IGOR;
            case "NegamaxMarina" -> botType = BotType.NEGAMAX_MARINA;
            case "ExpectimaxIgor" -> botType = BotType.EXPECTIMAX_IGOR;
            case "ExpectimaxMarina" -> botType = BotType.EXPECTIMAX_MARINA;

            default -> throw new IllegalArgumentException("Wrong Bot type selection");
        }

        return new StartGameDTO(clientGameType, clientColor, botType);
    }

    public static Bot createNewBot(BotType botType, Color color) {
        Bot bot;
        switch (botType) {
            case RANDOM -> bot = new Bot(color, 1, new GuiUserCommunicationService());
            case MINIMAX_IGOR -> bot = new MinimaxBot(color, 1, new GuiUserCommunicationService());
            case MINIMAX_MARINA -> bot = new MiniMaxBot(color, 1, new GuiUserCommunicationService());
            case NEGAMAX_IGOR -> bot = new NegamaxBot(color, 1, new GuiUserCommunicationService());
            case NEGAMAX_MARINA -> bot = new NegaMaxBot(color, 1, new GuiUserCommunicationService());
            case EXPECTIMAX_IGOR -> bot = new ExpectimaxBot(color, 1, new GuiUserCommunicationService());
            case EXPECTIMAX_MARINA -> bot = new ExpectiMaxBot(color, 1, new GuiUserCommunicationService());
            default -> throw new IllegalStateException("Can't create new Bot");
        }

        return bot;
    }

    public static io.deeplay.communication.model.SwitchPieceType getSwitchPieceTypeDTO(Move move) {
        String switchPieceType = move.switchPieceType().toString();

        return switch (switchPieceType) {
            case "ROOK" -> io.deeplay.communication.model.SwitchPieceType.ROOK;
            case "KNIGHT" -> io.deeplay.communication.model.SwitchPieceType.KNIGHT;
            case "QUEEN" -> io.deeplay.communication.model.SwitchPieceType.QUEEN;
            case "BISHOP" -> io.deeplay.communication.model.SwitchPieceType.BISHOP;
            case "NULL" -> io.deeplay.communication.model.SwitchPieceType.NULL;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static Color convertColor(io.deeplay.communication.model.Color color) {
        String colorString = color.toString();

        return switch (colorString) {
            case "BLACK" -> Color.BLACK;
            case "WHITE" -> Color.WHITE;
            case "EMPTY" -> Color.EMPTY;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static io.deeplay.communication.model.Color convertColorFromString(String color) {
        return switch (color) {
            case "WHITE" -> io.deeplay.communication.model.Color.WHITE;
            case "BLACK" -> io.deeplay.communication.model.Color.BLACK;
            case "EMPTY" -> io.deeplay.communication.model.Color.EMPTY;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static GameStates convertEndGameDTOToGameStates(EndGameDTO endGameDTO) {
        String endGameStateType = endGameDTO.getEndGameStateType().toString();
        return switch (endGameStateType) {
            case "CHECK" -> GameStates.CHECK;
            case "CHECKMATE" -> GameStates.CHECKMATE;
            case "STALEMATE" -> GameStates.STALEMATE;
            case "DEFAULT" -> GameStates.DEFAULT;
            case "DRAW" -> GameStates.DRAW;
            case "SURRENDER" -> GameStates.SURRENDER;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static EndGameDTO convertListEndGameToEndGameDTO(List<String> gameEnd) {
        String gameState = gameEnd.get(0);

        GameStateType gameStateType = switch (gameState) {
            case "DRAW" -> GameStateType.DRAW;
            case "DEFAULT" -> GameStateType.DEFAULT;
            case "STALEMATE" -> GameStateType.STALEMATE;
            case "CHECK" -> GameStateType.CHECK;
            case "CHECKMATE" -> GameStateType.CHECKMATE;
            case "SURRENDER" -> GameStateType.SURRENDER;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };

        return new EndGameDTO(gameStateType, convertColorFromString(gameEnd.get(1)));
    }


    public static GameType convertGameTypeDTO(io.deeplay.communication.model.GameType gameType) {
        String gameTypeString = gameType.toString();
        return switch (gameTypeString) {
            case "BotVsBot" -> GameType.BotVsBot;
            case "HumanVsBot" -> GameType.HumanVsBot;
            case "HumanVsHuman" -> GameType.HumanVsHuman;

            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static Move convertDTOToMove(MoveDTO moveDTO) {
        int startPositionX = moveDTO.getStartPosition().getX();
        int startPositionY = moveDTO.getStartPosition().getY();
        int endPositionX = moveDTO.getEndPosition().getX();
        int endPositionY = moveDTO.getEndPosition().getY();
        return new Move(new Coordinates(startPositionX, startPositionY), new Coordinates(endPositionX, endPositionY),
                getMoveTypeFromDTO(moveDTO), getSwitchPieceType(moveDTO));
    }

    public static MoveDTO convertMoveToMoveDTO(Move move) {
        int startPositionX = move.startPosition().getX();
        int startPositionY = move.startPosition().getY();
        int endPositionX = move.endPosition().getX();
        int endPositionY = move.endPosition().getY();
        return new MoveDTO(new io.deeplay.communication.model.Coordinates(startPositionX, startPositionY),
                new io.deeplay.communication.model.Coordinates(endPositionX, endPositionY),
                getMoveTypeFromMove(move), getSwitchPieceTypeDTO(move));
    }

    public static List<String> convertEndGameStateDTO(EndGameDTO endGameDTO) {
        List<String> result = new ArrayList<>();
        String gameStates = convertEndGameDTOToGameStates(endGameDTO).toString();
        String winColor = convertColor(endGameDTO.getWinColor()).toString();

        result.add(gameStates);
        result.add(winColor);

        return result;
    }

    public static ErrorResponseDTO convertErrorToErrorResponseDTO(Exception exception, String message) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(exception, message);
        errorResponseDTO.setException(exception);
        errorResponseDTO.setMessage(message);
        return errorResponseDTO;
    }

    public static List<Object> convertErrorResponseDTOToList(ErrorResponseDTO errorResponseDTO) {
        List<Object> errorList = new ArrayList<>();
        errorList.add(errorResponseDTO.getException());
        errorList.add(errorResponseDTO.getMessage());
        return errorList;
    }
}
