package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuGUI extends JFrame {
    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 400;
    private JComboBox<String> gameTypeComboBox;
    private JComboBox<String> botDifficultyComboBox;
    private JButton chooseColorButton;
    private JButton startGameButton;
    private JButton exitButton;

    public MainMenuGUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Chess Main Menu");
        setSize(APP_WIDTH, APP_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 1, 10, 10));
//        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Выравнивание по центру с отступом 20px
        setFont(new Font("Arial", Font.PLAIN, 16));

        String[] gameTypes = {"Человек vs. Человек", "Бот vs. Бот", "Человек vs. Бот"};
        gameTypeComboBox = new JComboBox<>(gameTypes);
        gameTypeComboBox.setMaximumSize(new Dimension(50, 10));
        add(new JLabel("Выберите тип игры:"));
        add(gameTypeComboBox);

        String[] botDifficulties = {"Легкий", "Средний", "Тяжелый"};
        botDifficultyComboBox = new JComboBox<>(botDifficulties);
        botDifficultyComboBox.setEnabled(false);
        add(new JLabel("Выберите сложность бота:"));
        add(botDifficultyComboBox);

        chooseColorButton = new JButton("Выберите цвета игроков");
        add(chooseColorButton);

        startGameButton = new JButton("Start Game");
        add(startGameButton);

        exitButton = new JButton("Выход");
        add(exitButton);

        gameTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedGameType = (String) gameTypeComboBox.getSelectedItem();
                if (selectedGameType.equals("Бот vs. Бот") || selectedGameType.equals("Человек vs. Бот")) {
                    botDifficultyComboBox.setEnabled(true);
                } else {
                    botDifficultyComboBox.setEnabled(false);
                }
            }
        });

        chooseColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] colors = {"White", "Black"};
                String selectedColor = (String) JOptionPane.showInputDialog(
                        MainMenuGUI.this,
                        "Choose a color for Player 1:",
                        "Player 1 Color",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        colors,
                        "White");

                if (selectedColor != null) {
                    // TODO: Обработать выбранный цвет игрока 1
                    System.out.println("Player 1 color: " + selectedColor);
                }
            }
        });

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Добавить вызов метода для запуска игры с выбранными настройками
                System.out.println("Starting the game...");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // TODO: Implement action listener for chooseColorButton to handle color selection.

        int windowHeight = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - APP_HEIGHT) / 2);
        int windowWidth = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - APP_WIDTH) / 2);

        setLocation(windowWidth, windowHeight);
        setVisible(true);
    }
}