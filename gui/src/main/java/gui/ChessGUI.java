package gui;

import gui.model.PieceColorIcon;
import gui.model.PlayerType;
import io.deeplay.client.Client;
import io.deeplay.communication.converter.Converter;
import io.deeplay.communication.dto.StartGameDTO;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
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
import io.deeplay.service.GuiUserCommunicationService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ChessGUI extends JFrame implements EndpointUser {
    private final int BOARD_SIZE = 8;
    private final Color HIGHLIGHT_COLOR = Color.YELLOW;
    private final Color POSSIBLEMOVE_COLOR = Color.GREEN;
    private JPanel chessBoardPanel;
    private JButton[][] chessBoardSquares;
    private Color[][] defaultCellColors;
    private JTextArea moveHistoryTextArea;
    private JLabel currentPlayerLabel;
    private JButton selectedSquare = null;
    private Piece prevSelectedPiece = null;
    private Map<Coordinates, Boolean> possibleMoveCells = null;
    private boolean isPlayerMove;
    private final GameInfo gameInfo;
    private final Player player;
    private final Client client;
    private Coordinates selectedMoveCoordinates;

    /**
     * Конструктор класса. Задает начальные параметры переменных, переопределяет GuiUserCommunicationService. Затем,
     * в зависимости от выбранного типа игрока создает объект класса Human или выбранного бота. Создает нового клиента
     * и создает соединение с сервером. Дальше запускает start метод для бота или человека.
     * @param startGameDTO начальные параметры с главной страницы, выбранные пользователем
     * @param playerType тип игрока (Human или Bot)
     */
    public ChessGUI(StartGameDTO startGameDTO, PlayerType playerType) {
        io.deeplay.domain.Color currentColor = Converter.convertColor(startGameDTO.getCurrentColor());
        isPlayerMove = currentColor == io.deeplay.domain.Color.WHITE;
        this.gameInfo = new GameInfo(currentColor) {
            @Override
            protected void changeCurrentMoveColor() {

            }
        };
        GuiUserCommunicationService guiUserCommunicationService = new GuiUserCommunicationService() {
            @Override
            public Piece selectPiece(List<Piece> possiblePiecesToMove) {
                return prevSelectedPiece;
            }

            @Override
            public Coordinates selectCoordinates(List<Coordinates> availableMoves) {
                return selectedMoveCoordinates;
            }

            @Override
            public SwitchPieceType selectSwitchPiece() {
                return ChessSquareListener.getSwitchPieceType();
            }
        };

        if (playerType == PlayerType.HUMAN) {
            this.player = new Human(currentColor, guiUserCommunicationService);
        } else {
            this.player = Converter.createNewBot(startGameDTO.getBotType(), currentColor);
        }
        this.client = new Client(startGameDTO);
        client.connectToServer();
        initialize();

        if (player instanceof Bot) {
            startBot();
        } else {
            startPlayer();
        }
    }

    /**
     * Метод инициализирует графический интерфейс
     */
    public void initialize() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chessBoardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        chessBoardPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        chessBoardSquares = new JButton[BOARD_SIZE][BOARD_SIZE];
        defaultCellColors = new Color[BOARD_SIZE][BOARD_SIZE];

        paintInitialBoard(gameInfo.getCurrentBoard().getBoard());

        moveHistoryTextArea = new JTextArea(20, 18);
        moveHistoryTextArea.setEditable(false);

        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setText("Текущий ход: БЕЛЫЙ");
        add(currentPlayerLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chessBoardPanel, new JScrollPane(moveHistoryTextArea));
        splitPane.setDividerLocation(490);
        add(splitPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    /**
     * Приватный класс, который добавляет слушателя для каждой из 64 клеток на игровом поле.
     * Отвечает за обработку нажатий на игровом поле.
     */
    private class ChessSquareListener implements ActionListener {
        private final int x;
        private final int y;

        public ChessSquareListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedSquare = (JButton) e.getSource();
            Board currentBoard = gameInfo.getCurrentBoard();
            Piece selectedPiece = currentBoard.getPiece(new Coordinates(x, y));

            io.deeplay.domain.Color currentColor = gameInfo.getCurrentMoveColor();
            List<Coordinates> possibleMoves = null;
            if (selectedPiece.getColor() == currentColor) {
                possibleMoves = selectedPiece.getPossibleMoves(currentBoard);
                List<Coordinates> movesWithoutCheck = GameState.getMovesWithoutMakingCheck(gameInfo.getCurrentBoard(), selectedPiece, possibleMoves);
                possibleMoves.retainAll(movesWithoutCheck);
            }

            if (selectedSquare == null) {
                if (isPlayerMove) {
                    prevSelectedPiece = selectedPiece;
                    selectedSquare = clickedSquare;
                    selectedSquare.setBackground(HIGHLIGHT_COLOR);
                    if (possibleMoves != null) {
                        possibleMoveCells = new HashMap<>();
                        for (Coordinates coordinates : possibleMoves) {
                            possibleMoveCells.put(coordinates, true);
                            chessBoardSquares[coordinates.getX()][coordinates.getY()].setBackground(POSSIBLEMOVE_COLOR);
                        }
                    }
                }
            } else if (selectedSquare == chessBoardSquares[x][y]) {

                if (possibleMoves != null) {
                    for (Coordinates coordinates : possibleMoves) {
                        chessBoardSquares[coordinates.getX()][coordinates.getY()]
                                .setBackground(defaultCellColors[coordinates.getX()][coordinates.getY()]);
                    }
                }

                selectedSquare.setBackground(defaultCellColors[x][y]);
                selectedSquare = null;
            } else {
                for (Map.Entry<Coordinates, Boolean> coordinates : possibleMoveCells.entrySet()) {
                    if (selectedPiece.getCoordinates().getX() == coordinates.getKey().getX()
                            && selectedPiece.getCoordinates().getY() == coordinates.getKey().getY()) {
                        selectedMoveCoordinates = selectedPiece.getCoordinates();
                        Move move = player.getMove(currentBoard,
                                prevSelectedPiece.getCoordinates(), selectedPiece.getCoordinates());
                        if (move.moveType() == MoveType.PROMOTION) {
                            SwitchPieceType switchPieceType = getSwitchPieceType();
                            Move promotionMove = new Move(move.startPosition(), move.endPosition(),
                                    move.moveType(), switchPieceType);
                            gameInfo.move(promotionMove);
                            client.sendMove(promotionMove);
                        } else {
                            gameInfo.move(move);
                            client.sendMove(move);
                        }
                        isPlayerMove = false;
                        addToMoveHistory(currentColor, move.startPosition(), move.endPosition());
                        possibleMoveCells = new HashMap<>();
                    }
                }
                selectedSquare.setBackground(chessBoardSquares[x][y].getBackground());
                selectedSquare = null;
                prevSelectedPiece = null;
                paintBoard(gameInfo.getCurrentBoard().getBoard());
                switchColorAppearance();

                waitAndUpdate();
            }
        }

        /**
         * Метод вызывает всплывающее окно из класса PromotionOptionPane для того, чтобы пользователь выбрал фигуру
         * для promotion
         * @return выбранную пользователем фигуру или ошибку
         */
        public static SwitchPieceType getSwitchPieceType() {
            String result = new PromotionOptionPane().getResult();
            SwitchPieceType switchPieceType;
            switch (result) {
                case "BISHOP" -> switchPieceType = SwitchPieceType.BISHOP;
                case "KNIGHT" -> switchPieceType = SwitchPieceType.KNIGHT;
                case "QUEEN" -> switchPieceType = SwitchPieceType.QUEEN;
                case "ROOK" -> switchPieceType = SwitchPieceType.ROOK;
                default -> throw new IllegalArgumentException("Invalid choice");
            }
            return switchPieceType;
        }
    }

    /**
     * Метод для старта бота на графическом интерфейсе. Интерфейс, если ход противоположного бота, ждет запроса от
     * сервера с ходом игрока, и при поступлении хода - обновляет доску. Затем, когда ход текущего бота, получает Move,
     * обновляет доску и отправляет запрос на сервер с этим Move,
     * после чего переходит в режим ожидания запроса от сервера.
     */
    public void startBot() {
        new Thread(() -> {
            while (true) {
                if (!isPlayerMove) {
                    waitAndUpdate();
                } else {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        Move move = player.getMove(gameInfo.getCurrentBoard(), player.getColor());

                        gameInfo.move(move);
                        isPlayerMove = false;
                        switchColorAppearance();
                        addToMoveHistory(player.getColor(), move.startPosition(), move.endPosition());
                        paintBoard(gameInfo.getCurrentBoard().getBoard());

                        client.sendMove(move);
                        waitAndUpdate();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    /**
     * Метод для старта графического интерфейса для игрока.
     */
    public void startPlayer() {
        if (!isPlayerMove) {
            waitAndUpdate();
        }
    }

    /**
     * Метод ожидает запрос от сервера, а затем вызывает функцию обработки этого запроса.
     */
    public void waitAndUpdate() {
        new Thread(() -> {
            Object playerAction = client.startListening();
            processClientInfo(playerAction);
        }).start();
    }

    /**
     * Метод обрабатывает входящий запрос от сервера в зависимости от его типа.
     * @param action входящий запрос в виде объекта
     */
    public void processClientInfo(Object action) {
        if (action instanceof Move move) {
            updateGameInfo(move);
        } else if (action instanceof List<?>) {
            System.out.println("game over in wait and update");
            List<String> endGameInfo = (List<String>) action;
            endGame(endGameInfo);
        }
    }

    /**
     * Метод обрабатывает конец игры, путем вызова всплывающего окна.
     * При нажатии на "Ок" или крестик - закрывает приложение
     * @param endGameInfo информация об окончании игры
     */
    public void endGame(List<String> endGameInfo) {
        String gameStates = endGameInfo.get(0);
        String winColor = endGameInfo.get(1);

        JFrame frame = new JFrame("Игра завершена");

        JLabel gameStatesLabel = new JLabel("Состояние игры: " + gameStates);
        JLabel winColorLabel = new JLabel("Победитель: " + winColor);

        JPanel panel = new JPanel();
        panel.add(gameStatesLabel);
        panel.add(winColorLabel);

        frame.getContentPane().add(panel);
        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Обновляет GameInfo, вызывает функцию добавления хода в историю,
     * отрисовывает заново доску, передает ход текущему игроку.
     * @param incomingMove ход противоположного игрока
     */
    public void updateGameInfo(Move incomingMove) {
        gameInfo.move(incomingMove);
        isPlayerMove = true;
        switchColorAppearance();
        addToMoveHistory(player.getColor().opposite(), incomingMove.startPosition(), incomingMove.endPosition());
        paintBoard(gameInfo.getCurrentBoard().getBoard());
    }

    /**
     * Добавляет ход в историю
     * @param color цвет хода
     * @param start начальная позиция
     * @param end конечная позиция
     */
    private void addToMoveHistory(io.deeplay.domain.Color color, Coordinates start, Coordinates end) {
        moveHistoryTextArea.append(" " + color + ": " + start + " -> " + end + "\n");
    }

    /**
     * Отрисовывает начальную доску, задает actionListener для клеток поля
     * @param board начальная доска
     */
    public void paintInitialBoard(Piece[][] board) {
        boolean isLightSquare = false;

        if (player.getColor() == io.deeplay.domain.Color.WHITE) {
            for (int y = 7; y >= 0; y--) {
                isLightSquare = !isLightSquare;
                for (int x = 0; x < BOARD_SIZE; x++) {
                    isLightSquare = !isLightSquare;
                    chessBoardSquares[x][y] = new JButton();
                    chessBoardSquares[x][y].setPreferredSize(new Dimension(60, 60));

                    if (board[x][y].getClass() != Empty.class) {
                        chessBoardSquares[x][y].setIcon(setIcon(board[x][y].getClass().getSimpleName(), board[x][y].getColor()));
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
                }
            }
        } else {
            for (int y = 0; y <= 7; y++) {
                isLightSquare = !isLightSquare;
                for (int x = 7; x >= 0; x--) {
                    isLightSquare = !isLightSquare;
                    chessBoardSquares[x][y] = new JButton();
                    chessBoardSquares[x][y].setPreferredSize(new Dimension(60, 60));

                    if (board[x][y].getClass() != Empty.class) {
                        chessBoardSquares[x][y].setIcon(setIcon(board[x][y].getClass().getSimpleName(), board[x][y].getColor()));
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
                }
                System.out.println();
            }
        }
    }

    /**
     * Отрисовывает текущую доску
     * @param board текущая доска
     */
    private void paintBoard(Piece[][] board) {
        boolean isLightSquare = false;

        if (player.getColor() == io.deeplay.domain.Color.WHITE) {
            for (int y = 7; y >= 0; y--) {
                isLightSquare = !isLightSquare;
                for (int x = 0; x < BOARD_SIZE; x++) {
                    isLightSquare = !isLightSquare;
                    if (board[x][y].getClass() != Empty.class) {
                        chessBoardSquares[x][y].setIcon(setIcon(board[x][y].getClass().getSimpleName(), board[x][y].getColor()));
                    } else {
                        chessBoardSquares[x][y].setIcon(null);
                    }

                    if (isLightSquare) {
                        chessBoardSquares[x][y].setBackground(new Color(0xFF5E1700, true));
                    } else {
                        chessBoardSquares[x][y].setBackground(new Color(0xFFB74D00, true));
                    }
                }
            }
        } else {
            for (int y = 0; y <= 7; y++) {
                isLightSquare = !isLightSquare;
                for (int x = 7; x >= 0; x--) {
                    isLightSquare = !isLightSquare;
                    if (board[x][y].getClass() != Empty.class) {
                        chessBoardSquares[x][y].setIcon(setIcon(board[x][y].getClass().getSimpleName(), board[x][y].getColor()));
                    } else {
                        chessBoardSquares[x][y].setIcon(null);
                    }

                    if (isLightSquare) {
                        chessBoardSquares[x][y].setBackground(new Color(0xFF5E1700, true));
                    } else {
                        chessBoardSquares[x][y].setBackground(new Color(0xFFB74D00, true));
                    }
                }
            }
        }
    }

    /**
     * Задает иконки для фигур
     * @param className название класса фигуры
     * @param color цвет фигуры
     * @return иконку фигуры
     */
    Icon setIcon(String className, io.deeplay.domain.Color color) {
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

    /**
     * Метод отвечает за смену отображения текущего хода.
     */
    private void switchColorAppearance() {
        io.deeplay.domain.Color playerColor = player.getColor();

        if (playerColor.equals(io.deeplay.domain.Color.WHITE)) {
            if (isPlayerMove) currentPlayerLabel.setText("Текущий ход: БЕЛЫЙ");
            else currentPlayerLabel.setText("Текущий ход: ЧЕРНЫЙ");
        } else {
            if (isPlayerMove) currentPlayerLabel.setText("Текущий ход: ЧЕРНЫЙ");
            else currentPlayerLabel.setText("Текущий ход: БЕЛЫЙ");
        }
    }
}