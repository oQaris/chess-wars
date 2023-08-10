package io.deeplay.service;

import io.deeplay.domain.Color;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameSession;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Human;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class UserCommunicationServiceTest {
    @Test
    void testGetGameSessionInfo_expectNewGameSession() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("bot-bot".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        GameSession gameSession = userCommunicationService.getGameSessionInfo();

        Assertions.assertInstanceOf(GameSession.class, gameSession);
    }

    @Test
    void testGetGameSessionInfo_expectThrowNewIllegalArgumentException() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("wewewe".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
                userCommunicationService::getGameSessionInfo);
        Assertions.assertEquals("Invalid input. Ending...", thrown.getMessage());
    }

    @Test
    void testSelectPiece_expectNewPiece() {
        Board board = new Board();
        Human human = new Human(Color.WHITE);
        List<Piece> possiblePiecesToMove = human.getPiecesPossibleToMove(board, human.getColor());

        ByteArrayInputStream inputStream = new ByteArrayInputStream("1".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        Piece selectedPiece = userCommunicationService.selectPiece(possiblePiecesToMove);
        Assertions.assertTrue(possiblePiecesToMove.contains(selectedPiece));
    }

    @Test
    void testSelectPiece_expectThrowNewIllegalArgumentException() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("105".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        Board board = new Board();
        Human human = new Human(Color.WHITE);
        List<Piece> possiblePiecesToMove = human.getPiecesPossibleToMove(board, human.getColor());

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userCommunicationService.selectPiece(possiblePiecesToMove));
        Assertions.assertEquals("Invalid choice!", thrown.getMessage());
    }

    @Test
    void testSelectCoordinates_expectNewCoordinates() {
        List<Coordinates> coordinatesList = new ArrayList<>();
        coordinatesList.add(new Coordinates(1,2));
        coordinatesList.add(new Coordinates(0,3));
        coordinatesList.add(new Coordinates(3,1));

        ByteArrayInputStream inputStream = new ByteArrayInputStream("1".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        Coordinates selectedCoordinates = userCommunicationService.selectCoordinates(coordinatesList);
        Assertions.assertTrue(coordinatesList.contains(selectedCoordinates));
        Assertions.assertNotEquals(1, selectedCoordinates.getX());
    }

    @Test
    void testSelectCoordinates_expectThrowNewIllegalArgumentException() {
        List<Coordinates> coordinatesList = new ArrayList<>();
        coordinatesList.add(new Coordinates(1,2));
        coordinatesList.add(new Coordinates(0,3));
        coordinatesList.add(new Coordinates(3,1));

        ByteArrayInputStream inputStream = new ByteArrayInputStream("1111".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userCommunicationService.selectCoordinates(coordinatesList));
        Assertions.assertEquals("Invalid choice!", thrown.getMessage());
    }

    @Test
    void testSelectSwitchPiece_expectNewSwitchPieceType() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("1".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        SwitchPieceType switchPieceType = userCommunicationService.selectSwitchPiece();

        Assertions.assertEquals(SwitchPieceType.QUEEN, switchPieceType);
    }

    @Test
    void testSelectSwitchPiece_expectThrowNewIllegalArgumentException() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("105".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
                userCommunicationService::selectSwitchPiece);
        Assertions.assertEquals("Invalid choice!", thrown.getMessage());
    }

    @Test
    @Disabled
    void testChooseBotLevel() {
    }

    @Test
    void testChooseColor_expectNewColor() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("w".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        Color[] colors = userCommunicationService.chooseColor();

        Assertions.assertArrayEquals(new Color[] {Color.WHITE, Color.BLACK}, colors);
        Assertions.assertNotEquals(Color.BLACK, colors[0]);
        Assertions.assertNotEquals(Color.WHITE, colors[1]);
    }

    @Test
    void testChooseColor_expectThrowNewIllegalArgumentException() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("pink".getBytes(Charset.defaultCharset()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        UserCommunicationService userCommunicationService = new UserCommunicationService(inputStream, ps);

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
                userCommunicationService::chooseColor);
        Assertions.assertEquals("Invalid color choice!", thrown.getMessage());
    }
}