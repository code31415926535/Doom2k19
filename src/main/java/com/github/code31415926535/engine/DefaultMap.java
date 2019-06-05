package com.github.code31415926535.engine;

import com.github.code31415926535.engine.primitives.PointOfView;
import com.github.code31415926535.engine.primitives.Sector;
import com.github.code31415926535.engine.primitives.Vertex;

import java.awt.*;
import java.util.HashMap;

public class DefaultMap extends Map {
    public DefaultMap(HashMap<String, Sector> sectors, Vertex pov, double angle, String povSector) {
        super(sectors, new PointOfView(pov, angle, sectors.get(povSector)));
    }

    public void render(Graphics2D g2d, int w, int h) {}
}
