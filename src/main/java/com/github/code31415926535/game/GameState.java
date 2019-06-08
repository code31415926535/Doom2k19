package com.github.code31415926535.game;

import java.awt.*;
import java.awt.event.KeyEvent;

abstract class GameState {
    GameStateManager gsm;
    final int width;
    final int height;

    GameState(GameStateManager parent, int width, int height) {
        gsm = parent;

        this.width = width;
        this.height = height;
    }

    abstract void keyPressed(KeyEvent e);
    abstract void keyReleased(KeyEvent e);
    abstract void update();
    abstract void render(Graphics2D g2d);

    void forward(GameState state) {
        gsm.pushState(state);
    }

    void back() {
        gsm.popState();
    }
}
