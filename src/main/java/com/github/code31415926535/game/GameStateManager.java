package com.github.code31415926535.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Stack;

public class GameStateManager {
    private Stack<GameState> gameStateStack;

    public GameStateManager(int w, int h) {
        gameStateStack = new Stack<>();
        pushState(new MenuState(
                this,
                w,
                h
        ));
    }

    void pushState(GameState state) {
        gameStateStack.push(state);
    }

    void popState() {
        gameStateStack.pop();

        // TODO: Maybe move this somewhere else
        if (gameStateStack.empty()) {
            System.exit(0);
        }
    }

    public void keyPressed(KeyEvent e) {
        gameStateStack.peek().keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        gameStateStack.peek().keyReleased(e);
    }

    public void update() {
        gameStateStack.peek().update();
    }

    public void render(Graphics2D g2d) {
        gameStateStack.peek().render(g2d);
    }
}
