package gui;

import gui.model.PieceColorIcon;
import gui.service.BoardService;
import io.deeplay.domain.GameType;
import io.deeplay.engine.GameInfo;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.Empty;
import io.deeplay.model.piece.Piece;
import io.deeplay.model.player.Bot;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;
import io.deeplay.server.ServerPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static io.deeplay.model.Board.printBoardOnce;

public class ChessGUI extends JFrame {

    private JPanel chessBoardPanel;
    private JButton[][] chessBoardSquares;
    private Color[][] defaultCellColors;
    private JTextArea moveHistoryTextArea;
    private final int BOARD_SIZE = 8;
    private final Color LIGHT_SQUARE_COLOR = Color.WHITE;
    private final Color DARK_SQUARE_COLOR = Color.GRAY;
    private final Color HIGHLIGHT_COLOR = Color.YELLOW;
    private final Color POSSIBLEMOVE_COLOR = Color.GREEN;
    private JButton selectedSquare = null;
    private Piece prevSelectedPiece = null;
    private GameInfo gameInfo;
    private Player player;
    private List<Coordinates> possibleMoves;
    private Map<Coordinates, Boolean> possibleMoveCells = null;

    public ChessGUI(String gameType, String whitePlayerChoice, String botLevel) {
//        session = processAndCreateSession(gameType, whitePlayerChoice, botLevel);
        gameInfo = new GameInfo();
        player = new ServerPlayer(getColor(whitePlayerChoice));
        initUI();
    }

    private void initUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chessBoardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        chessBoardPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        chessBoardSquares = new JButton[BOARD_SIZE][BOARD_SIZE];
        defaultCellColors = new Color[BOARD_SIZE][BOARD_SIZE];

        paintBoard(BoardService.getBoard(gameInfo.getCurrentBoard()));

        moveHistoryTextArea = new JTextArea(20, 15);
        moveHistoryTextArea.setEditable(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chessBoardPanel, new JScrollPane(moveHistoryTextArea));
        splitPane.setDividerLocation(490);
        add(splitPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class ChessSquareListener implements ActionListener {
        private int y;
        private int x;

        public ChessSquareListener(int y, int x) {
            this.y = y;
            this.x = x;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedSquare = (JButton) e.getSource();
            Board currentBoard = gameInfo.getCurrentBoard();
            Piece selectedPiece = currentBoard.getPiece(new Coordinates(y, x));

            io.deeplay.domain.Color currentColor = gameInfo.getCurrentMoveColor();
            possibleMoves = null;
            if (selectedPiece.getColor() == currentColor) {
                possibleMoves = selectedPiece.getPossibleMoves(currentBoard);
            }

            if (selectedSquare == null) {
                System.out.println("Click to the cell");
                prevSelectedPiece = selectedPiece;
                selectedSquare = clickedSquare;
                selectedSquare.setBackground(HIGHLIGHT_COLOR);
                if (possibleMoves != null) {
                    possibleMoveCells = new HashMap<>();
                    for (Coordinates coordinates : possibleMoves) {
                        possibleMoveCells.put(coordinates, true);
                        chessBoardSquares[coordinates.getY()][coordinates.getX()].setBackground(POSSIBLEMOVE_COLOR);
                    }
                }
            } else if (selectedSquare == chessBoardSquares[x][y]) {
                System.out.println("deselect cell");

                if (possibleMoves != null) {
                    for (Coordinates coordinates : possibleMoves) {
                        chessBoardSquares[coordinates.getY()][coordinates.getX()]
                                .setBackground(defaultCellColors[coordinates.getY()][coordinates.getX()]);
                    }
                }

                selectedSquare.setBackground(defaultCellColors[x][y]);
                selectedSquare = null;
            } else {
                // Here you would implement the logic to make the move
                // based on the selectedSquare and clickedSquare positions
                // Update moveHistoryTextArea with the move details

                // Reset the selected square's background color

                for (Map.Entry<Coordinates, Boolean> coordinates : possibleMoveCells.entrySet()) {
                    chessBoardSquares[coordinates.getKey().getX()][coordinates.getKey().getY()]
                            .setBackground(defaultCellColors[coordinates.getKey().getX()][coordinates.getKey().getY()]);
                    if (selectedPiece.getCoordinates().getX() == coordinates.getKey().getX()
                            && selectedPiece.getCoordinates().getY() == coordinates.getKey().getY()) {
                        int pieceCol = prevSelectedPiece.getCoordinates().getX();
                        int pieceRow = prevSelectedPiece.getCoordinates().getY();
                        int moveCol = selectedPiece.getCoordinates().getX();
                        int moveRow = selectedPiece.getCoordinates().getY();

                        gameInfo.move(player.getMove(currentBoard, player.getColor(),
                                prevSelectedPiece.getCoordinates(), selectedPiece.getCoordinates()));

                        chessBoardSquares[pieceRow][pieceCol].setIcon(null);
                        chessBoardSquares[moveRow][moveCol].setIcon(setIcon(
                                currentBoard.getBoard()[moveCol][moveRow].getClass().getSimpleName(),
                                currentBoard.getBoard()[moveCol][moveRow].getColor()));
                    }

                    for (Map.Entry<Coordinates, Boolean> prevcoordinates : possibleMoveCells.entrySet()) {
                        chessBoardSquares[prevcoordinates.getKey().getY()][prevcoordinates.getKey().getX()]
                                .setBackground(defaultCellColors[prevcoordinates.getKey().getY()][prevcoordinates.getKey().getX()]);
                    }
                }
                selectedSquare.setBackground(chessBoardSquares[x][y].getBackground());
                selectedSquare = null;
                prevSelectedPiece = null;
            }
        }
    }

    public void updateGameInfo(Move move) {
        gameInfo.move(move);
    }

    public void paintBoard(Piece[][] board) {
        boolean isLightSquare = true;

        // Если в будущем будет вылетать ошибка, то тут должно быть так:
         for (int x = 0; x < BOARD_SIZE; x++) {
         isLightSquare = !isLightSquare;
         for (int y = 0; y < BOARD_SIZE; y++) {
//        for (int x = 7; x >= 0; x--) {
//            isLightSquare = !isLightSquare;
//            for (int y = 7; y >= 0; y--) {
                chessBoardSquares[x][y] = new JButton();
                chessBoardSquares[x][y].setPreferredSize(new Dimension(60, 60));

                if (board[y][x].getClass() != Empty.class) {
                    chessBoardSquares[x][y].setIcon(setIcon(board[y][x].getClass().getSimpleName(), board[y][x].getColor()));
                }

                if (isLightSquare) {
                    defaultCellColors[x][y] = new Color(0xFF5E1700, true);
                    chessBoardSquares[x][y].setBackground(new Color(0xFF5E1700, true));
                } else {
                    defaultCellColors[x][y] = new Color(0xFFB74D00, true);
                    chessBoardSquares[x][y].setBackground(new Color(0xFFB74D00, true));
                }

                chessBoardSquares[x][y].addActionListener(new ChessSquareListener(x, y));
                chessBoardPanel.add(chessBoardSquares[x][y]);
                isLightSquare = !isLightSquare;
            }
        }
    }

    public Icon setIcon(String className, io.deeplay.domain.Color color) {
        String path = "";
        if (color == io.deeplay.domain.Color.WHITE) {
            switch (className) {
                case "King" -> path = PieceColorIcon.WHITE_KING.path;
                case "Queen" -> path = PieceColorIcon.WHITE_QUEEN.path;
                case "Knight" -> path = PieceColorIcon.WHITE_KNIGHT.path;
                case "Bishop" -> path = PieceColorIcon.WHITE_BISHOP.path;
                case "Rook" -> path = PieceColorIcon.WHITE_ROOK.path;
                case "Pawn" -> path = PieceColorIcon.WHITE_PAWN.path;
            }
        } else {
            switch (className) {
                case "King" -> path = PieceColorIcon.BLACK_KING.path;
                case "Queen" -> path = PieceColorIcon.BLACK_QUEEN.path;
                case "Knight" -> path = PieceColorIcon.BLACK_KNIGHT.path;
                case "Bishop" -> path = PieceColorIcon.BLACK_BISHOP.path;
                case "Rook" -> path = PieceColorIcon.BLACK_ROOK.path;
                case "Pawn" -> path = PieceColorIcon.BLACK_PAWN.path;
            }
        }
        Image image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(image);
    }

    io.deeplay.domain.Color getColor(String whitePlayerChoice) {
        boolean isWhiteFirst;
        switch (whitePlayerChoice) {
            case "Игрок 1" -> isWhiteFirst = true;
            case "Игрок 2" -> isWhiteFirst = false;
            default -> throw new IllegalArgumentException("Wrong white player selection");
        }

        if (isWhiteFirst) return io.deeplay.domain.Color.BLACK;
        else return io.deeplay.domain.Color.WHITE;
    }

//    GuiSession processAndCreateSession(String gameTypeString, String whitePlayerChoice, String selectedBotLevel) {
//        int botLevel;
//        switch (selectedBotLevel) {
//            case "Легкий" -> botLevel = 1;
//            case "Средний" -> botLevel = 2;
//            case "Сложный" -> botLevel = 3;
//            case "" -> botLevel = 0;
//            default -> throw new IllegalArgumentException("Wrong bot level selection");
//        }
//
//        GameType gameType;
//        switch (gameTypeString) {
//            case "Человек vs. Человек" -> gameType = GameType.HumanVsHuman;
//            case "Бот vs. Бот" -> gameType = GameType.BotVsBot;
//            case "Человек vs. Бот" -> gameType = GameType.HumanVsBot;
//            default -> throw new IllegalArgumentException("Wrong game type selection");
//        }
//
//        boolean isWhiteFirst;
//        switch (whitePlayerChoice) {
//            case "Игрок 1" -> isWhiteFirst = true;
//            case "Игрок 2" -> isWhiteFirst = false;
//            default -> throw new IllegalArgumentException("Wrong white player selection");
//        }
//
//        Player player1;
//        Player player2;
//        if (gameType == GameType.HumanVsHuman) {
//            if (isWhiteFirst) {
//                player1 = new Human(io.deeplay.domain.Color.WHITE);
//                player2 = new Human(io.deeplay.domain.Color.BLACK);
//            } else {
//                player1 = new Human(io.deeplay.domain.Color.BLACK);
//                player2 = new Human(io.deeplay.domain.Color.WHITE);
//            }
//        } else if (gameType == GameType.BotVsBot) {
//            if (isWhiteFirst) {
//                player1 = new Bot(io.deeplay.domain.Color.WHITE, botLevel);
//                player2 = new Bot(io.deeplay.domain.Color.BLACK, botLevel);
//            } else {
//                player1 = new Bot(io.deeplay.domain.Color.BLACK, botLevel);
//                player2 = new Bot(io.deeplay.domain.Color.WHITE, botLevel);
//            }
//        } else {
//            if (isWhiteFirst) {
//                player1 = new Human(io.deeplay.domain.Color.WHITE);
//                player2 = new Bot(io.deeplay.domain.Color.BLACK, botLevel);
//            } else {
//                player1 = new Human(io.deeplay.domain.Color.BLACK);
//                player2 = new Bot(io.deeplay.domain.Color.WHITE, botLevel);
//            }
//        }
//
//        return new GuiSession(player1, player2, gameType);
//    }
}