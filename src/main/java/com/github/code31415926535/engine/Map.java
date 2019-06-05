package com.github.code31415926535.engine;

import com.github.code31415926535.engine.primitives.Action;
import com.github.code31415926535.engine.primitives.PointOfView;
import com.github.code31415926535.engine.primitives.Sector;

import java.awt.*;
import java.util.HashMap;

public abstract class Map {
    protected HashMap<String, Sector> sectors;

    protected PointOfView pointOfView;

    protected Map(HashMap<String, Sector> sectors, PointOfView pointOfView) {
        this.sectors = sectors;
        this.pointOfView = pointOfView;
    }

    public void updatePOV(Action action) {
        pointOfView.update(action);
    }

    public void update() {
        // TODO: Implement
    }

    public abstract void render(Graphics2D g2d, int w, int h);

    public Map toMap2D() {
        return new Map2D(this);
    }

    public Map toMap3D() {
        return new Map3D(this);
    }
}