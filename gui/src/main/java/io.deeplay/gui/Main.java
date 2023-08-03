package io.deeplay.gui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGUI chessGUI = new ChessGUI();
            chessGUI.setVisible(true);
        });
    }
}
