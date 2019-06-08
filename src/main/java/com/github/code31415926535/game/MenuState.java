package com.github.code31415926535.game;

class MenuState extends MenuBaseState {
    MenuState(GameStateManager gsm, int width, int height) {
        super(gsm, width, height, "Main Menu");
    }

    void loadEntries() {
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
}
