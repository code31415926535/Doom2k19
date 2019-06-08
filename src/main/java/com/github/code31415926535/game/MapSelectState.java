package com.github.code31415926535.game;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

// TODO: Make select pretty
public class MapSelectState extends MenuBaseState {
    // TODO: Customise font
    private static final Font font = new Font("TimesRoman", Font.PLAIN, 32);
    private static final Font titleFont = new Font("TimesRoman", Font.PLAIN, 48);
    private static final String MAPS_TITLE = "Select map";
    private static final String MAP_DIR_ROOT = "maps";
    private static final int ENTRIES_ON_SCREEN = 5;

    public MapSelectState(GameStateManager gsm, int width, int height) {
        super(gsm, width, height);
    }

    void init() {
        String filename = Objects.requireNonNull(getClass().getClassLoader().getResource(MAP_DIR_ROOT + "/")).getFile();
        File dir = new File(filename);
        if (!dir.isDirectory()) {
            try {
                throw new IOException("File is not a directory");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File[] files = dir.listFiles();
        for (File file: Objects.requireNonNull(files)) {
            if (file.isFile()) {
                entries.add(file.getName());
            }
        }
    }

    void select() {
        String selectedMap = entries.get(selectedEntryId);
        try {
            GameState playState = new PlayState(gsm, width, height, selectedMap);
            forward(playState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void render(Graphics2D g2d) {
        int titleHeight = height / 6;
        int mapHeight = (height / 6) * 5;
        int mapTotalHeight = entries.size()*font.getSize();
        int spaceHeight = (mapHeight - mapTotalHeight) / (entries.size()+1);

        g2d.setFont(titleFont);
        g2d.setColor(Color.GREEN);

        int textWidth = g2d.getFontMetrics().stringWidth(MAPS_TITLE);
        g2d.drawString(MAPS_TITLE, (width - textWidth) / 2, (titleHeight - titleFont.getSize()) / 2);

        int startEntry = selectedEntryId - ENTRIES_ON_SCREEN / 2;
        if (startEntry < 0) {
            startEntry = 0;

        }
        // TODO: Make this work for less than 5 files
        if (startEntry + ENTRIES_ON_SCREEN > entries.size() - 1) {
            startEntry -= (startEntry + ENTRIES_ON_SCREEN - entries.size());
        }

        g2d.setFont(font);
        for (int i = startEntry; i != startEntry+ENTRIES_ON_SCREEN; i ++) {
            g2d.setColor(Color.GREEN);
            if (i == selectedEntryId) {
                g2d.setColor(Color.RED);
            }
            String text = entries.get(i);
            textWidth = g2d.getFontMetrics().stringWidth(text);
            int startPosition = (i-startEntry+1)*spaceHeight + font.getSize()*(i-startEntry);
            g2d.drawString(text, (width - textWidth) / 2, titleHeight + startPosition);
        }
    }
}
