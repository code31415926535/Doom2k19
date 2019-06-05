package com.github.code31415926535.engine;

import com.github.code31415926535.engine.primitives.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO: Controls should be smoother
public class Controls {
    private static final int DEFAULT_RESPONSIVENESS = 5;

    private int responsiveness;
    private HashMap<Action, Integer> heldKeys;

    public Controls() {
        this(DEFAULT_RESPONSIVENESS);
    }

    public Controls(int responsiveness) {
        this.responsiveness = responsiveness;
        heldKeys = new HashMap<>();
    }

    public void startAction(Action action) {
        heldKeys.put(action, 0);
    }

    public void endAction(Action action) {
        heldKeys.remove(action);
    }

    public List<Action> update() {
        ArrayList<Action> result = new ArrayList<>();

        for (Action action: heldKeys.keySet()) {
            Integer value = heldKeys.get(action);
            if (value == 0) {
                result.add(action);
                heldKeys.replace(action, responsiveness);
            } else {
                heldKeys.replace(action, heldKeys.get(action) - 1);
            }
        }

        return result;
    }
}
