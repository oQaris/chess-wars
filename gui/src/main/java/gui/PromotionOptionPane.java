package gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class PromotionOptionPane {
    private static final Logger logger = LogManager.getLogger(PromotionOptionPane.class);

    /**
     * Вызывает всплывающее окно с выбором фигуры на promotion.
     * @return строку с названием выбранной фигуры.
     */
    public String getResult() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                logger.error(ex);
            }
        });
        JPanel panel = new JPanel();
        panel.add(new JLabel("Выберите фигуру, в которую хотите превратить пешку:"));
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("BISHOP");
        model.addElement("KNIGHT");
        model.addElement("QUEEN");
        model.addElement("ROOK");
        JComboBox<?> comboBox = new JComboBox<>(model);
        panel.add(comboBox);
        JOptionPane.showConfirmDialog(null, panel, "Promotion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return String.valueOf(comboBox.getSelectedItem());
    }
}