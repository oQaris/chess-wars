package gui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuGUI chessGUI = new MainMenuGUI();
            chessGUI.setVisible(true);
        });
    }
}
