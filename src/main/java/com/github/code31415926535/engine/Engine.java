package com.github.code31415926535.engine;

import com.github.code31415926535.engine.exceptions.InvalidMapFormatException;
import com.github.code31415926535.engine.primitives.Action;

import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.util.List;

public class Engine {
    private final int width;
    private final int height;

    private Controls controls;

    private Map map;
    private FpsMeter fpsMeter;

    public Engine(String map, int width, int height) throws FileNotFoundException, InvalidMapFormatException {
        this.width = width;
        this.height = height;

        this.controls = new Controls();

        this.map = new MapLoader(map).load().toMap3D();
        this.fpsMeter = new FpsMeter();
    }

    // Make movement continuous
    public void keyPressed(int keyCode) {
        Action action = Action.fromKeyCode(keyCode);

        if (action == Action.TOGGLE_FPS) {
            fpsMeter.toggle();
        } else if (action == Action.TOGGLE_2D_3D) {
            if ( map instanceof Map2D) {
                map = map.toMap3D();
            } else {
                map = map.toMap2D();
            }
        } else if (action.isMovement()) {
            controls.startAction(action);
        }
    }

    public void keyReleased(int keyCode) {
        Action action = Action.fromKeyCode(keyCode);

        if (action.isMovement()) {
            controls.endAction(action);
        }
    }

    public void update() {
        List<Action> actions = controls.update();
        for (Action action: actions) {
            map.updatePOV(action);
        }
        map.update();
        fpsMeter.update();
    }

    public void render(Graphics2D g2d) {
        map.render(g2d, width, height);
        fpsMeter.render(g2d, width, height);
    }
}
