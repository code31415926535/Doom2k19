package com.github.code31415926535.engine;

import com.github.code31415926535.engine.primitives.Sector;
import com.github.code31415926535.engine.primitives.Segment;
import com.github.code31415926535.engine.primitives.Vertex;

import java.awt.*;

public class Map2D extends Map {
    private static final int POINT_W = 4;
    private static final int ARROW_LENGTH = 6;

    // TODO: Make scale changeable by zoom
    private double scale = 2;

    public Map2D(Map map) {
        super(map.sectors, map.pointOfView);
    }

    public void render(Graphics2D g2d, int w, int h) {
        // TODO: Center everything around player
        Graphics2D transformed = (Graphics2D) g2d.create();

        transformed.scale(scale, scale);
        transformed.translate(w / (2*scale), h / (2*scale));
        render(transformed);
    }

    private void render(Graphics2D g2d) {
        drawSector(g2d);
        drawPlayer(g2d);
    }

    private void drawSector(Graphics2D g2d) {
        for (Sector sector: sectors.values()) {
            drawWalls(g2d, sector);
        }
    }

    private void drawFieldOfViewIntersection(Graphics2D g2d, Segment segment, Vertex lm, Vertex rm) {
        Segment fieldOfViewIntersection = segment.getFieldOfViewIntersection(pointOfView.getPoint(), lm, rm);
        if (fieldOfViewIntersection != null) {
            g2d.setColor(Color.YELLOW);
            drawLine(g2d, fieldOfViewIntersection.getA(), fieldOfViewIntersection.getB());
        }
    }

    private void drawWalls(Graphics2D g2d, Sector sector) {
        g2d.setStroke(new BasicStroke(2));

        Vertex leftMargin = pointOfView.getFOVLeftMargin();
        Vertex rightMargin = pointOfView.getFOVRightMargin();
        for (Segment segment : sector.getSegments()) {
            if (segment.getNeighbour() != null) {
                g2d.setColor(new Color(64, 0, 0));
                drawLine(g2d, segment.getA(), segment.getB());
            } else {
                g2d.setColor(Color.RED);
                drawLine(g2d, segment.getA(), segment.getB());
                drawFieldOfViewIntersection(g2d, segment, leftMargin, rightMargin);
            }
        }

        g2d.setStroke(new BasicStroke(1));
    }

    private void drawPlayer(Graphics2D g2d) {
        drawPOV(g2d);
        drawFOV(g2d);
    }

    private void drawPOV(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(
                pointOfView.getPoint().getRoundedX() - POINT_W / 2,
                pointOfView.getPoint().getRoundedY() - POINT_W / 2,
                POINT_W,
                POINT_W);

        Segment segment = pointOfView.getPoint().perpendicularLine(pointOfView.getAngleOfView());
        drawLine(g2d, segment.getA(), segment.getB());

        Vertex dest = pointOfView.getPoint().moveByDistanceAndAngle(ARROW_LENGTH, pointOfView.getAngleOfView());
        drawLine(g2d, pointOfView.getPoint(), dest);
    }

    private void drawFOV(Graphics2D g2d) {
        Vertex leftMargin = pointOfView.getFOVLeftMargin();
        Vertex rightMargin = pointOfView.getFOVRightMargin();

        drawLine(g2d, pointOfView.getPoint(), leftMargin);
        drawLine(g2d, pointOfView.getPoint(), rightMargin);
    }

    private void drawLine(Graphics2D g2d, Vertex v1, Vertex v2) {
        g2d.drawLine(
                v1.getRoundedX(),
                v1.getRoundedY(),
                v2.getRoundedX(),
                v2.getRoundedY()
        );
    }
}
