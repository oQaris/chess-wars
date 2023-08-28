package io.deeplay.communication.converter;

import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.communication.model.GameStateType;
import io.deeplay.domain.*;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Converter {

    public static MoveType getMoveTypeFromDTO(MoveDTO moveDTO) {
        if (Objects.equals(moveDTO.getMoveType().toString(), "EN_PASSANT")) {
            return MoveType.EN_PASSANT;
        }
        if (Objects.equals(moveDTO.getMoveType().toString(), "ORDINARY")) {
            return MoveType.ORDINARY;
        }
        if (Objects.equals(moveDTO.getMoveType().toString(), "CASTLING")) {
            return MoveType.CASTLING;
        }
        if (Objects.equals(moveDTO.getMoveType().toString(), "PROMOTION")) {
            return MoveType.PROMOTION;
        }
        if (Objects.equals(moveDTO.getMoveType().toString(), "TAKE")) {
            return MoveType.TAKE;
        }
        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static io.deeplay.communication.model.MoveType getMoveTypeFromMove(Move move) {
        if (Objects.equals(move.moveType().toString(), "EN_PASSANT")) {
            return io.deeplay.communication.model.MoveType.EN_PASSANT;
        }
        if (Objects.equals(move.moveType().toString(), "ORDINARY")) {
            return io.deeplay.communication.model.MoveType.ORDINARY;
        }
        if (Objects.equals(move.moveType().toString(), "CASTLING")) {
            return io.deeplay.communication.model.MoveType.CASTLING;
        }
        if (Objects.equals(move.moveType().toString(), "PROMOTION")) {
            return io.deeplay.communication.model.MoveType.PROMOTION;
        }
        if (Objects.equals(move.moveType().toString(), "TAKE")) {
            return io.deeplay.communication.model.MoveType.TAKE;
        }
        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static SwitchPieceType getSwitchPieceType(MoveDTO moveDTO) {
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "ROOK")) {
            return SwitchPieceType.ROOK;
        }
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "KNIGHT")) {
            return SwitchPieceType.KNIGHT;
        }
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "QUEEN")) {
            return SwitchPieceType.QUEEN;
        }
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "BISHOP")) {
            return SwitchPieceType.BISHOP;
        }
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "NULL")) {
            return SwitchPieceType.NULL;
        }
        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static StartGameDTO getStartGameSettings(List<String> gameSettings) {
        io.deeplay.communication.model.GameType clientGameType;
        io.deeplay.communication.model.Color clientColor;
        int clientBotLevel;

        switch (gameSettings.get(0)) {
            case "Человек vs. Человек" -> clientGameType = io.deeplay.communication.model.GameType.HumanVsHuman;
            case "Человек vs. Бот" -> clientGameType = io.deeplay.communication.model.GameType.HumanVsBot;
            case "Бот vs. Бот" -> clientGameType = io.deeplay.communication.model.GameType.BotVsBot;
            default -> throw new IllegalArgumentException("Wrong Game Type selection");
        }

        switch (gameSettings.get(1)) {
            case "Белый" -> clientColor = io.deeplay.communication.model.Color.WHITE;
            case "Черный" -> clientColor = io.deeplay.communication.model.Color.BLACK;
            default -> throw new IllegalArgumentException("Wrong Color selection");
        }

        switch (gameSettings.get(2)) {
            case "Легкий" -> clientBotLevel = 1;
            case "Средний" -> clientBotLevel = 2;
            case "Сложный" -> clientBotLevel = 3;
            default -> throw new IllegalArgumentException("Wrong Bot Level selection");
        }

        return new StartGameDTO(clientGameType, clientColor, clientBotLevel);
    }

    public static io.deeplay.communication.model.SwitchPieceType getSwitchPieceTypeDTO(Move move) {
        if (Objects.equals(move.switchPieceType().toString(), "ROOK")) {
            return io.deeplay.communication.model.SwitchPieceType.ROOK;
        }
        if (Objects.equals(move.switchPieceType().toString(), "KNIGHT")) {
            return io.deeplay.communication.model.SwitchPieceType.KNIGHT;
        }
        if (Objects.equals(move.switchPieceType().toString(), "QUEEN")) {
            return io.deeplay.communication.model.SwitchPieceType.QUEEN;
        }
        if (Objects.equals(move.switchPieceType().toString(), "BISHOP")) {
            return io.deeplay.communication.model.SwitchPieceType.BISHOP;
        }
        if (Objects.equals(move.switchPieceType().toString(), "NULL")) {
            return io.deeplay.communication.model.SwitchPieceType.NULL;
        }
        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static Color convertColor(io.deeplay.communication.model.Color color) {
        if (Objects.equals(color.toString(), Color.BLACK.toString())) {
            return Color.BLACK;
        }
        if (Objects.equals(color.toString(), Color.WHITE.toString())) {
            return Color.WHITE;
        }
        if (Objects.equals(color.toString(), Color.EMPTY.toString())) {
            return Color.EMPTY;
        }
        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static io.deeplay.communication.model.Color convertColorFromString(String color) {
        if (color.equals("WHITE")){
            return io.deeplay.communication.model.Color.WHITE ;
        }

        if (color.equals("BLACK")){
            return io.deeplay.communication.model.Color.BLACK;
        }

        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static GameStates convertEndGameDTOToGameStates(EndGameDTO endGameDTO) {
        if (Objects.equals(endGameDTO.getEndGameStateType().toString(), GameStates.CHECK.toString())) {
            return GameStates.CHECK;
        }

        if (Objects.equals(endGameDTO.getEndGameStateType().toString(), GameStates.CHECKMATE.toString())) {
            return GameStates.CHECKMATE;
        }

        if (Objects.equals(endGameDTO.getEndGameStateType().toString(), GameStates.STALEMATE.toString())) {
            return GameStates.STALEMATE;
        }

        if (Objects.equals(endGameDTO.getEndGameStateType().toString(), GameStates.DEFAULT.toString())) {
            return GameStates.DEFAULT;
        }

        if (Objects.equals(endGameDTO.getEndGameStateType().toString(), GameStates.DRAW.toString())) {
            return GameStates.DRAW;
        }

        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static EndGameDTO convertListEndGameToEndGameDTO(List<String> gameEnd) {
        if (gameEnd.get(0).equals(GameStates.DRAW.toString())) {
            return new EndGameDTO(GameStateType.DRAW, convertColorFromString(gameEnd.get(1)));
        }

        if (gameEnd.get(0).equals(GameStates.DEFAULT.toString())) {
            return new EndGameDTO(GameStateType.DEFAULT, convertColorFromString(gameEnd.get(1)));
        }

        if (gameEnd.get(0).equals(GameStates.STALEMATE.toString())) {
            return new EndGameDTO(GameStateType.STALEMATE, convertColorFromString(gameEnd.get(1)));
        }

        if (gameEnd.get(0).equals(GameStates.CHECK.toString())) {
            return new EndGameDTO(GameStateType.CHECK, convertColorFromString(gameEnd.get(1)));
        }

        if (gameEnd.get(0).equals(GameStates.CHECKMATE.toString())) {
            return new EndGameDTO(GameStateType.CHECKMATE, convertColorFromString(gameEnd.get(1)));
        }

        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static GameStates convertGameStateTypeToGameStates(io.deeplay.communication.model.GameStateType gameStateType) {
        if (Objects.equals(gameStateType.toString(), "CHECK")) {
            return GameStates.CHECK;
        }

        if (Objects.equals(gameStateType.toString(), "DRAW")) {
            return GameStates.DRAW;
        }

        if (Objects.equals(gameStateType.toString(), "DEFAULT")) {
            return GameStates.DEFAULT;
        }

        if (Objects.equals(gameStateType.toString(), "MATE")) {
            return GameStates.CHECKMATE;
        }

        if (Objects.equals(gameStateType.toString(), "STALEMATE")) {
            return GameStates.STALEMATE;
        }

        throw new IllegalArgumentException("Illegal parameter converting");
    }

    public static GameType convertGameTypeDTO(io.deeplay.communication.model.GameType gameType) {
        if (Objects.equals(gameType.toString(), "BotVsBot")) {
            return GameType.BotVsBot;
        }

        if (Objects.equals(gameType.toString(), "HumanVsBot")) {
            return GameType.HumanVsBot;
        }

        if (Objects.equals(gameType.toString(), "HumanVsHuman")) {
            return GameType.HumanVsHuman;
        }

        throw new IllegalArgumentException("Illegal parameter converting");
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

    public static List<String> convertEndGameStateDTO(EndGameDTO endGameDTO){
        List<String> result = new ArrayList<>();
        String gameStates = convertEndGameDTOToGameStates(endGameDTO).toString();
        String winColor = convertColor(endGameDTO.getWinColor()).toString();

        result.add(gameStates);
        result.add(winColor);

        return result;
    }

    public static String convertStartGameDTO(StartGameDTO startGameDTO) {
        return Objects.requireNonNull(convertGameTypeDTO(startGameDTO.getGameType()))
                + "," + Objects.requireNonNull(convertColor(startGameDTO.getCurrentColor()));
    }
}
