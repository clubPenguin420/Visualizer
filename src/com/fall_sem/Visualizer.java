package com.fall_sem;

import javax.swing.*;
import javax.swing.event.*;

public class Visualizer {

    private static void initGUI() {
        JFrame frame = new JFrame("Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        frame.setSize(500, 500);
        frame.setVisible(true);
    }
    public static void main(String[] args) {

        SwingUtilities.invokeLater(Visualizer::initGUI);
    }
}
