package com.github.code31415926535.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

abstract class MenuBaseState extends GameState {
    private static final Font OPTIONS_FONT = Fonts.MENU_FONT;
    private static final Font TITLE_FONT = Fonts.TITLE_FONT;

    final List<String> entries = new ArrayList<>();
    private final String title;
    private final int entriesPerScreen;
    int selectedEntryId;

    private final int titleHeight;
    private final int mapHeight;

    MenuBaseState(GameStateManager parent, int width, int height, String title) {
        this(parent, width, height, title, -1);
    }

    MenuBaseState(GameStateManager parent, int width, int height, String title, int entriesPerScreen) {
        super(parent, width, height);
        loadEntries();

        this.title = title;
        if (entriesPerScreen <= 0) {
            this.entriesPerScreen = entries.size();
        } else {
            this.entriesPerScreen = entriesPerScreen;
        }

        titleHeight = height / 6;
        mapHeight = height - titleHeight;
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

    void render(Graphics2D g2d) {
        renderTitle(g2d);
        renderEntries(g2d);
    }

    private void renderTitle(Graphics2D g2d) {
        g2d.setFont(TITLE_FONT);
        g2d.setColor(Color.RED);
        drawStringCenter(g2d, title, TITLE_FONT.getSize() / 2 + (titleHeight - TITLE_FONT.getSize()) / 2);
    }

    private void renderEntries(Graphics2D g2d) {
        int startEntry = calculateStartEntry();
        int count = entriesPerScreen;

        int entriesHeight = entriesPerScreen*OPTIONS_FONT.getSize();
        int spaceHeight = (mapHeight - entriesHeight) / (entriesPerScreen+1);

        g2d.setFont(OPTIONS_FONT);
        for (int i = startEntry; i != startEntry + count; i ++) {
            g2d.setColor(Color.GREEN);
            if (i == selectedEntryId) {
                g2d.setColor(Color.RED);
            }
            int h = titleHeight + OPTIONS_FONT.getSize() / 2 + (i-startEntry+1)*spaceHeight + OPTIONS_FONT.getSize()*(i-startEntry);
            drawStringCenter(g2d, entries.get(i), h);
        }
    }

    private int calculateStartEntry() {
        int startEntry = selectedEntryId - entriesPerScreen / 2;
        if (startEntry < 0) {
            startEntry = 0;
        }

        if (startEntry + entriesPerScreen > entries.size() -1) {
            startEntry -= (startEntry + entriesPerScreen - entries.size());
        }

        return startEntry;
    }

    private void drawStringCenter(Graphics2D g2d, String string, int height) {
        int textWidth = g2d.getFontMetrics().stringWidth(string);
        g2d.drawString(string, (width - textWidth) / 2, height);
    }

    abstract void loadEntries();
    abstract void select();
}
