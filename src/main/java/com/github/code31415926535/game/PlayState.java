package com.github.code31415926535.game;

import com.github.code31415926535.engine.Engine;
import com.github.code31415926535.engine.exceptions.InvalidMapFormatException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

public class PlayState extends GameState {
    private Engine engine;

    public PlayState(GameStateManager gsm, int w, int h, String map) throws FileNotFoundException, InvalidMapFormatException {
        super(gsm, w, h);
        this.engine = new Engine(map, w, h);
    }

    void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            back();
            return;
        }

        engine.keyPressed(e.getKeyCode());
    }

    void keyReleased(KeyEvent e) {
        engine.keyReleased(e.getKeyCode());
    }

    void update() {
        engine.update();
    }

    void render(Graphics2D g2d) {
        engine.render(g2d);
    }
}
