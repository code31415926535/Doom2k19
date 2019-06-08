package com.github.code31415926535.game;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class GameState {
    protected GameStateManager gsm;
    protected final int width;
    protected final int height;

    GameState(GameStateManager parent, int width, int height) {
        gsm = parent;

        this.width = width;
        this.height = height;
    }

    abstract void keyPressed(KeyEvent e);
    abstract void keyReleased(KeyEvent e);
    abstract void update();
    abstract void render(Graphics2D g2d);

    protected void forward(GameState state) {
        gsm.pushState(state);
    }

    protected void back() {
        gsm.popState();
    }
}
