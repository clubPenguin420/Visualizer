package com.fall_sem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visualizer {
    private JPanel Base_Panel;
    private JTabbedPane tabbedPane1;
    private JRadioButton radioButton1;
    private JPanel Base_Conversion;
    private JTextPane Welcome_Text;

    public Visualizer() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Visualizer");
        frame.setContentPane(new Visualizer().Base_Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(600, 600);
        frame.setLocation(1000, 250);
        frame.setVisible(true);
    }
}
