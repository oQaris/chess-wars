package gui;

import gui.model.PieceColorIcon;
import gui.service.BoardService;
import io.deeplay.client.Client;
import io.deeplay.domain.MoveType;
import io.deeplay.domain.SwitchPieceType;
import io.deeplay.engine.GameInfo;
import io.deeplay.engine.GameState;
import io.deeplay.model.Board;
import io.deeplay.model.Coordinates;
import io.deeplay.model.move.Move;
import io.deeplay.model.piece.*;
import io.deeplay.model.player.Human;
import io.deeplay.model.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ChessGUI extends JFrame {

    private JPanel chessBoardPanel;
    private JButton[][] chessBoardSquares;
    private Color[][] defaultCellColors;
    private JTextArea moveHistoryTextArea;
    private final int BOARD_SIZE = 8;
    private final Color HIGHLIGHT_COLOR = Color.YELLOW;
    private final Color POSSIBLEMOVE_COLOR = Color.GREEN;
    private JButton selectedSquare = null;
    private Piece prevSelectedPiece = null;
    private GameInfo gameInfo;
    private Player player;
    private List<Coordinates> possibleMoves;
    private Map<Coordinates, Boolean> possibleMoveCells = null;
    private Client client;

    // Поменять передачу цвета на передачу Player в конструкторе
    public ChessGUI(String gameType, String whitePlayerChoice, String botLevel) {
        gameInfo = new GameInfo();
        player = new Human(getColor(whitePlayerChoice));
        initUI();
    }

    private void initUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chessBoardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        chessBoardPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        chessBoardSquares = new JButton[BOARD_SIZE][BOARD_SIZE];
        defaultCellColors = new Color[BOARD_SIZE][BOARD_SIZE];

        paintInitialBoard(BoardService.getBoard(gameInfo.getCurrentBoard()));

        moveHistoryTextArea = new JTextArea(20, 18);
        moveHistoryTextArea.setEditable(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chessBoardPanel, new JScrollPane(moveHistoryTextArea));
        splitPane.setDividerLocation(490);
        add(splitPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private class ChessSquareListener implements ActionListener {
        private int x;
        private int y;

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
            possibleMoves = null;
            if (selectedPiece.getColor() == currentColor) {
                possibleMoves = selectedPiece.getPossibleMoves(currentBoard);
                List<Coordinates> movesWithoutCheck = GameState.getMovesWithoutMakingCheck(gameInfo.getCurrentBoard(), selectedPiece, possibleMoves);
                possibleMoves.retainAll(movesWithoutCheck);
            }

            if (selectedSquare == null) {
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
                        Move move = player.getMove(currentBoard, player.getColor(),
                                prevSelectedPiece.getCoordinates(), selectedPiece.getCoordinates());
                        if (move.moveType() == MoveType.PROMOTION) {
                            SwitchPieceType switchPieceType = getSwitchPieceType();
                            gameInfo.move(new Move(move.startPosition(), move.endPosition(),
                                    move.moveType(), switchPieceType));
                        } else {
                            gameInfo.move(move);
                        }
                        addToMoveHistory(currentColor, move.startPosition(), move.endPosition());
                        possibleMoveCells = new HashMap<>();
                    }
                }
                selectedSquare.setBackground(chessBoardSquares[x][y].getBackground());
                selectedSquare = null;
                prevSelectedPiece = null;
                paintBoard(gameInfo.getCurrentBoard().getBoard());
                Board.printBoardOnce(gameInfo.getCurrentBoard());
            }
        }

        private SwitchPieceType getSwitchPieceType() {
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

    private void paintBoard(Piece[][] board) {
        boolean isLightSquare = false;

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
    }

    public void updateGameInfo(Move move) {
        gameInfo.move(move);
    }

    private void addToMoveHistory(io.deeplay.domain.Color color, Coordinates start, Coordinates end) {
        moveHistoryTextArea.append(" " + color + ": " + start + " -> " + end + "\n");
    }

    public void paintInitialBoard(Piece[][] board) {
        boolean isLightSquare = false;

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
            case "Белый" -> isWhiteFirst = true;
            case "Черный" -> isWhiteFirst = false;
            default -> throw new IllegalArgumentException("Wrong white player selection");
        }

        if (isWhiteFirst) return io.deeplay.domain.Color.BLACK;
        else return io.deeplay.domain.Color.WHITE;
    }
}