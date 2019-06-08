package com.github.code31415926535.game;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

class MapSelectState extends MenuBaseState {
    private static final String MAP_DIR_ROOT = "maps";
    private static final int ENTRIES_ON_SCREEN = 5;

    MapSelectState(GameStateManager gsm, int width, int height) {
        super(gsm, width, height, "Select map", ENTRIES_ON_SCREEN);
    }

    void loadEntries() {
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
}
