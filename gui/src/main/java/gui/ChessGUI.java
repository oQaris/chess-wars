package gui;

import gui.service.BoardService;
import io.deeplay.model.piece.Empty;
import io.deeplay.model.piece.Piece;

import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {

    private JPanel chessBoardPanel;
    private JButton[][] chessBoardSquares;
    private JTextArea moveHistoryTextArea;

    private final int BOARD_SIZE = 8;
    private final Color LIGHT_SQUARE_COLOR = Color.WHITE;
    private final Color DARK_SQUARE_COLOR = Color.GRAY;
    private final Color HIGHLIGHT_COLOR = Color.YELLOW;

    public ChessGUI() {
        initUI();
    }

    private void initUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chessBoardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        chessBoardPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        chessBoardSquares = new JButton[BOARD_SIZE][BOARD_SIZE];
        boolean isLightSquare = true;
        Piece[][] board = BoardService.getBoard();

        for (int row = 0; row < BOARD_SIZE; row++) {
            isLightSquare = !isLightSquare;
            for (int col = 0; col < BOARD_SIZE; col++) {
                chessBoardSquares[row][col] = new JButton();
                chessBoardSquares[row][col].setPreferredSize(new Dimension(60, 60));

                if (board[col][row].getClass() != Empty.class) {
                    chessBoardSquares[row][col].setText(String.valueOf(board[col][row].getClass().getSimpleName().charAt(0)));
                }

                if (isLightSquare) {
                    chessBoardSquares[row][col].setBackground(new Color(0xFF5E1700, true));
                } else {
                    chessBoardSquares[row][col].setBackground(new Color(0xFFB74D00, true));
                }

                chessBoardPanel.add(chessBoardSquares[row][col]);
                isLightSquare = !isLightSquare;
            }
        }

        moveHistoryTextArea = new JTextArea(20, 15);
        moveHistoryTextArea.setEditable(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chessBoardPanel, new JScrollPane(moveHistoryTextArea));
        splitPane.setDividerLocation(490);
        add(splitPane);

        pack();
        setLocationRelativeTo(null);
    }
}