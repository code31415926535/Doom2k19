package com.github.code31415926535.engine.primitives;

import java.awt.event.KeyEvent;

public enum Action {
    NOP,

    MOVE_FORWARD,
    MOVE_BACKWARD,
    ROTATE_LEFT,
    ROTATE_RIGHT,

    TOGGLE_FPS,
    TOGGLE_2D_3D;

    // TODO: Move this out and make it configurable
    public static Action fromKeyCode(int code) {
        switch (code) {
            case KeyEvent.VK_F:
                return TOGGLE_FPS;
            case KeyEvent.VK_M:
                return TOGGLE_2D_3D;

            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                return MOVE_FORWARD;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                return MOVE_BACKWARD;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                return ROTATE_LEFT;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                return ROTATE_RIGHT;
        }

        return NOP;
    }

    public boolean isMovement() {
        return (this == MOVE_FORWARD || this == MOVE_BACKWARD || this == ROTATE_LEFT || this == ROTATE_RIGHT);
    }
}