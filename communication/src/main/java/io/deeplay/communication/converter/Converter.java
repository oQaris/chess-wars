package io.deeplay.communication.converter;

import io.deeplay.communication.dto.EndGameDTO;
import io.deeplay.communication.dto.MoveDTO;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.domain.*;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;

import java.io.IOException;
import java.util.Objects;

public class Converter {

    public static MoveType getMoveTypeFromDTO(MoveDTO moveDTO) throws IOException {
        if (Objects.equals(moveDTO.getMoveType().toString(), "EN_PASSANT")){
            return MoveType.EN_PASSANT;
        }
        if (Objects.equals(moveDTO.getMoveType().toString(), "ORDINARY")){
            return MoveType.ORDINARY;
        }
        if (Objects.equals(moveDTO.getMoveType().toString(), "CASTLING")){
            return  MoveType.CASTLING;
        }
        if (Objects.equals(moveDTO.getMoveType().toString(), "PROMOTION")){
            return MoveType.PROMOTION;
        }
        if (Objects.equals(moveDTO.getMoveType().toString(), "TAKE")){
            return MoveType.TAKE;
        }throw new IOException("no such type");
    }

    public static SwitchPieceType getSwitchPieceType(MoveDTO moveDTO) throws IOException {
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "ROOK")){
            return SwitchPieceType.ROOK;
        }
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "KNIGHT")){
            return SwitchPieceType.KNIGHT;
        }
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "QUEEN")){
            return SwitchPieceType.QUEEN;
        }
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "BISHOP")){
            return SwitchPieceType.BISHOP;
        }
        if (Objects.equals(moveDTO.getSwitchPieceType().toString(), "NULL")){
            return SwitchPieceType.NULL;
        }throw new IOException("no such piece");
    }

    public static Color convertColor(io.deeplay.communication.model.Color color) throws IOException {
        if (Objects.equals(color.toString(), Color.BLACK.toString())){
            return Color.BLACK;
        }
        if (Objects.equals(color.toString(), Color.WHITE.toString())){
            return Color.WHITE;
        }
        if (Objects.equals(color.toString(), Color.EMPTY.toString())){
            return Color.EMPTY;
        }
        throw new IOException("no such Color");
    }

    public static GameStates convertGameStateDTO(EndGameDTO gameStateType) throws IOException {
        if (Objects.equals(gameStateType.getEndGameStateType().toString(), GameStates.CHECK.toString())){
            return GameStates.CHECK;
        }
        if (Objects.equals(gameStateType.getEndGameStateType().toString(), GameStates.CHECKMATE.toString())){
            return GameStates.CHECKMATE;
        }
        if (Objects.equals(gameStateType.getEndGameStateType().toString(), GameStates.STALEMATE.toString())){
            return GameStates.STALEMATE;
        }
        if (Objects.equals(gameStateType.getEndGameStateType().toString(), GameStates.DEFAULT.toString())){
            return GameStates.DEFAULT;
        }
        throw new IOException("no such gameState");
    }

    public static GameType convertGameTypeDTO(io.deeplay.communication.model.GameType gameType) throws IOException {
        if (Objects.equals(gameType.toString(), "BotVsBot")){
            return GameType.BotVsBot;
        }
        if (Objects.equals(gameType.toString(), "HumanVsBot")){
            return GameType.HumanVsBot;
        }
        if (Objects.equals(gameType.toString(), "HumanVsHuman")){
            return GameType.HumanVsHuman;
        }
        throw new IOException("no such GameType");
    }
    public static Move convertDTOToMove(MoveDTO moveDTO) throws IOException {
        int startPositionX = moveDTO.getStartPosition().getX();
        int startPositionY = moveDTO.getStartPosition().getY();
        int endPositionX = moveDTO.getEndPosition().getX();
        int endPositionY = moveDTO.getEndPosition().getY();
        return new Move(new Coordinates(startPositionX, startPositionY),  new Coordinates(endPositionX, endPositionY), getMoveTypeFromDTO(moveDTO), getSwitchPieceType(moveDTO));
    }

    public static String convertEndGameStateDTO(EndGameDTO endGameDTO) throws IOException {
        return Objects.requireNonNull(convertGameStateDTO(endGameDTO)) + "," + convertColor(endGameDTO.getWinColor());
    }

    public static String convertStartGameDTO(StartGameDTO startGameDTO) throws IOException {
        return Objects.requireNonNull(convertGameTypeDTO(startGameDTO.getGameType())) + "," + Objects.requireNonNull(convertColor(startGameDTO.getCurrentColor()));
    }
}
