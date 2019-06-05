package com.github.code31415926535;

import javax.swing.JFrame;

public class Window {

    public static void main(String[] args) {
        new Window();
    }

    private Window() {
        GamePanel panel = new GamePanel();

        JFrame frame = new JFrame("Doom2k19");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

