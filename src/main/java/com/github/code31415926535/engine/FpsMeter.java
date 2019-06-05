package com.github.code31415926535.engine;

import java.awt.*;

public class FpsMeter {
    // TODO: Stabilize meter
    private static final double PREV_RATIO = 0.8;
    private static final double MARGIN_OF_ERROR = 0.75;
    // TODO: Customize font
    private static final Font font = new Font("TimesRoman", Font.PLAIN, 32);

    private long oldTime;
    private double fps;

    private boolean enabled;

    public FpsMeter() {
        oldTime = System.nanoTime();
        fps = 0;

        enabled = true;
    }

    public void update() {
        long currentTime = System.nanoTime();
        double delta = (currentTime - oldTime) / 1000000.0;
        double measuredFps = 1000 / delta;
        double newFps = measuredFps*(1-PREV_RATIO) + fps*PREV_RATIO;
        if (Math.abs(newFps - fps) > MARGIN_OF_ERROR) {
            fps = newFps;
        }
        oldTime = currentTime;
    }

    public void render(Graphics2D g2d, int w, int h) {
        if (enabled) {
            _render(g2d, w, h);
        }
    }

    public void toggle() {
        enabled = !enabled;
    }

    private void _render(Graphics2D g2d, int w, int h) {
        String text = "" + Math.round(fps);

        int textWidth = g2d.getFontMetrics().stringWidth(text);

        g2d.setColor(Color.GREEN);
        g2d.setFont(font);
        g2d.drawString(text, w-textWidth, font.getSize());
    }
}
