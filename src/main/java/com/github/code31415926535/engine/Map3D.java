package com.github.code31415926535.engine;

import com.github.code31415926535.engine.primitives.GeomUtils;
import com.github.code31415926535.engine.primitives.Sector;
import com.github.code31415926535.engine.primitives.SectorRenderDetails;
import com.github.code31415926535.engine.primitives.Segment;
import com.github.code31415926535.engine.primitives.Vertex;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;

public class Map3D extends Map {
    public Map3D(Map map) {
        super(map.sectors, map.pointOfView);
    }

    public void render(Graphics2D g2d, int w, int h) {
        Vertex fieldStart = pointOfView.getFOVLeftMargin();
        Vertex fieldEnd = pointOfView.getFOVRightMargin();

        int[] top = new int[w];
        Arrays.fill(top, 0);

        int[] bottom = new int[w];
        Arrays.fill(bottom, h-1);

        SectorRenderDetails sectorRenderDetails = new SectorRenderDetails(pointOfView.getCurrentSector(), null, 0, w-1);
        drawSectors(g2d, w, h, fieldStart, fieldEnd, sectorRenderDetails, top, bottom);
    }

    private void drawSectors(Graphics2D g2d, int w, int h, Vertex fieldStart, Vertex fieldEnd,
                            SectorRenderDetails start, int[] top, int[] bottom) {
        HashSet<String> renderedSectors = new HashSet<>();

        Queue<SectorRenderDetails> sectorsToRender = new ArrayDeque<>();
        sectorsToRender.add(start);

        while (!sectorsToRender.isEmpty()) {
            SectorRenderDetails sectorRenderDetails = sectorsToRender.remove();
            renderedSectors.add(sectorRenderDetails.getSector().getName());
            ArrayList<SectorRenderDetails> neighbours = drawSector(g2d, w, h, fieldStart, fieldEnd, sectorRenderDetails, top, bottom);
            for (SectorRenderDetails srd: neighbours) {
                if (!renderedSectors.contains(srd.getSector().getName())) {
                    sectorsToRender.add(srd);
                }
            }
        }
    }

    private ArrayList<SectorRenderDetails> drawSector(Graphics2D g2d, int w, int h, Vertex fieldStart, Vertex fieldEnd,
                            SectorRenderDetails sectorRenderDetails, int[] top, int[] bottom) {
        // One step in angles a.k.a radius of 1 screen pixel
        double angleStep = pointOfView.getFieldOfView() / w;
        double distanceToProjectionPlane = (w / 2.0) / Math.tan(pointOfView.getFieldOfView() / 2);

        double fieldStartSlope = fieldStart.slopeWith(pointOfView.getPoint());
        double fieldEndSlope = fieldEnd.slopeWith(pointOfView.getPoint());
        double diff = GeomUtils.normalizeAngle(fieldEndSlope - fieldStartSlope);

        Sector sector = sectorRenderDetails.getSector();
        Sector parent = sectorRenderDetails.getParent();
        int minX = sectorRenderDetails.getXStart();
        int maxX = sectorRenderDetails.getXEnd();

        ArrayList<SectorRenderDetails>  neighbours = new ArrayList<>();

        for (Segment segment : sector.getSegments()) {
            Sector neighbour = segment.getNeighbour();
            // Skip for sector that connects to parent
            if (neighbour != null && neighbour == parent) {
                continue;
            }

            // Calculate intersection with field of view
            Segment fieldOfViewIntersection = segment.getFieldOfViewIntersection(pointOfView.getPoint(), fieldStart, fieldEnd);
            if (fieldOfViewIntersection != null) {
                Vertex s = fieldOfViewIntersection.getA();
                Vertex e = fieldOfViewIntersection.getB();
                // For each segment, calculate view and range on screen
                // angle: (segStart --> segEnd) 0 --> 1.047 (pointOfView.getFieldOfView())
                // range: (xStart --> xEnd) 0 --> w
                double segStart = GeomUtils.normalizeAngle(s.slopeWith(pointOfView.getPoint()) - fieldStartSlope) - diff;
                double segEnd = GeomUtils.normalizeAngle(e.slopeWith(pointOfView.getPoint()) - fieldStartSlope) - diff;

                // Switch points if start is after end
                if (segStart > segEnd) {
                    double temp = segStart;
                    segStart = segEnd;
                    segEnd = temp;
                    Vertex tmp = s;
                    s = e;
                    e = tmp;
                }

                int xStart = (int) Math.round(segStart / angleStep);
                int xEnd = (int) Math.round(segEnd / angleStep);

                // Bound xStart and xEnd
                xStart = GeomUtils.clamp(xStart, minX, maxX);
                xEnd = GeomUtils.clamp(xEnd, minX, maxX);

                if (xEnd - xStart > 0 && neighbour != null) {
                    neighbours.add(new SectorRenderDetails(neighbour, sector, xStart, xEnd));
                }

                for (int x = xStart; x != xEnd; x++) {
                    // absolute angle of direction. Since we are looking in an arc of pointOfView.getFieldOfView()
                    // width in direction that is projected onto the screen.
                    double angle = pointOfView.getAngleOfView() + x*angleStep - pointOfView.getFieldOfView() / 2;
                    // intersect (origin, angle) with (s, e)
                    Vertex originAngle = pointOfView.getPoint().moveByDistanceAndAngle(5, angle);
                    Vertex intersection = GeomUtils.intersection(pointOfView.getPoint(), originAngle, s, e);
                    // normalize distance to avoid fisheye effect.
                    double dist = pointOfView.getPoint().distanceTo(intersection)*Math.cos(pointOfView.getAngleOfView() - angle);

                    double height = sector.getHeight() / dist * distanceToProjectionPlane;
                    drawSlice(g2d, x, h, height, sector, neighbour, top, bottom);
                }
            }
        }

        return neighbours;
    }

    private void drawSlice(Graphics2D g2d, int x, int h, double height, Sector sector, Sector neighbour, int[] top, int[] bottom) {
        int start = (int) Math.round((h - height) / 2);
        int end = (int) Math.round((h + height) / 2);

        // draw ceiling
        drawVLine(g2d, x, top[x], start-1, Color.BLUE);
        // draw floor
        drawVLine(g2d, x, end+1, bottom[x], Color.GRAY);

        if (neighbour != null) {
            // draw stuff for neighbour sector
            int neighbourCeilingEnd = start;
            int neighbourFloorStart = end;

            double floorDiff = neighbour.getFloor() - sector.getFloor();
            if (floorDiff > 0) {
                neighbourFloorStart = end - (int) Math.round(height * (floorDiff / sector.getHeight()));
                drawVLine(g2d, x, neighbourFloorStart, end, Color.WHITE);
            }

            double ceilDiff = sector.getFloor() - neighbour.getFloor();
            if (ceilDiff > 0) {
                neighbourCeilingEnd = start - (int) Math.round(height * (floorDiff / sector.getHeight()));
                drawVLine(g2d, x, start, neighbourCeilingEnd, Color.WHITE);
            }

            top[x] = neighbourCeilingEnd;
            bottom[x] = neighbourFloorStart;

        } else {
            // draw wall
            drawVLine(g2d, x, start, end, Color.WHITE);
        }
    }

    private void drawVLine(Graphics2D g2d, int x, int y1, int y2, Color c) {
        g2d.setColor(c);
        g2d.drawLine(x, y1, x, y2);
    }
}
