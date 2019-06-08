package com.github.code31415926535.game;

import java.awt.*;

// TODO: Make menu pretty
public class MenuState extends MenuBaseState {
    // TODO: Customise font
    private static final Font font = new Font("TimesRoman", Font.PLAIN, 32);

    MenuState(GameStateManager gsm, int width, int height) {
        super(gsm, width, height);
    }

    void init() {
        entries.add("Play");
        entries.add("Edit");
        entries.add("Quit");
    }

    void select() {
        switch (selectedEntryId) {
            // Play
            case 0:
            case 1:
                try {
                    GameState mapSelectState = new MapSelectState(gsm, width, height);
                    forward(mapSelectState);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                back();
                break;
        }
    }

    void render(Graphics2D g2d) {
        // total height of text
        int totalHeight = entries.size()*font.getSize();
        // size of spacing
        int spacingHeight = (height - totalHeight) / (entries.size()+2);

        g2d.setFont(font);
        for (int i = 0; i != entries.size(); i ++) {
            g2d.setColor(Color.GREEN);
            if (i == selectedEntryId) {
                g2d.setColor(Color.RED);
            }
            String text = entries.get(i);
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (width - textWidth) / 2, spacingHeight*(i+1) + font.getSize()*i);
        }
    }
}
