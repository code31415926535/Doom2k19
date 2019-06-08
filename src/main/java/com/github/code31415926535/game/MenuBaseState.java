package com.github.code31415926535.game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

abstract class MenuBaseState extends GameState {
    final List<String> entries = new ArrayList<>();
    int selectedEntryId;

    MenuBaseState(GameStateManager parent, int width, int height) {
        super(parent, width, height);
        init();
    }

    void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                back();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                selectedEntryId ++;
                if (selectedEntryId > entries.size()- 1) {
                    selectedEntryId = 0;
                }
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                selectedEntryId --;
                if (selectedEntryId < 0) {
                    selectedEntryId = entries.size() -1;
                }
                break;
            case KeyEvent.VK_ENTER:
                select();
                break;
        }
    }

    void keyReleased(KeyEvent e) {}

    void update() {}

    abstract void init();
    abstract void select();
    abstract void render(Graphics2D g2d);
}
