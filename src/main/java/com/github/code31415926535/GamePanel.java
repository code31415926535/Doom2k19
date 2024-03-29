package com.github.code31415926535;

import com.github.code31415926535.game.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    // TODO: params here
    private final int width;
    private final int height;

    private boolean running;
    private Thread thread;

    private BufferedImage buffer;
    private Graphics2D g2d;

    private GameStateManager gsm;

    public GamePanel() {
        this(1280, 960);
    }

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));

        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init() {
        running = true;

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) buffer.getGraphics();

        try {
            gsm = new GameStateManager(width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        init();

        int FPS = 60;
        int targetTime = 1000 / FPS;

        while (running) {
            long startTime = System.nanoTime();
            update();
            render();
            draw();
            long endTime = System.nanoTime();
            // Convert from nano to milliseconds
            long duration = (endTime - startTime) / 1000000;
            long waitTime = targetTime - duration;
            if (waitTime < 0) {
                waitTime = 0;
            }
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        gsm.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        gsm.keyReleased(e);
    }

    public void keyTyped(KeyEvent e) {}

    private void update() {
        gsm.update();
    }

    private void render() {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        gsm.render(g2d);
    }

    private void draw() {
        Graphics g = getGraphics();
        g.drawImage(buffer, 0, 0, null);
        g.dispose();
    }
}
