package gui;

import javax.swing.*;
import java.awt.*;

public class PromotionOptionPane {
    private JPanel panel;

    public String getResult() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }
        });
        panel = new JPanel();
        panel.add(new JLabel("Выберите фигуру, в которую хотите превратить пешку:"));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("BISHOP");
        model.addElement("KNIGHT");
        model.addElement("QUEEN");
        model.addElement("ROOK");
        JComboBox comboBox = new JComboBox(model);
        panel.add(comboBox);
        JOptionPane.showConfirmDialog(null, panel, "Promotion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return String.valueOf(comboBox.getSelectedItem());
    }
}