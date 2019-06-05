package com.github.code31415926535.engine.primitives;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class Sector {
    @Getter
    private double floor;
    @Getter
    private double ceil;

    @Getter
    private List<Segment> segments;

    public void addNeighbour(Segment segment, Sector sector) {
        for (int i = 0; i != segments.size(); i ++) {
            if (segments.get(i).equals(segment)) {
                segments.get(i).setNeighbour(sector);
            }
        }
    }

    public boolean isVertexInside(Vertex v) {
        int intersectionCount = 0;

        Vertex dist = v.moveByDistanceAndAngle(GeomUtils.INFINITE, 0);

        for (Segment segment: segments) {
            Vertex intersection = GeomUtils.intersection(v, dist, segment.getA(), segment.getB());
            if (segment.isOnSegment(intersection) && new Segment(v, dist).isOnSegment(intersection)) {
                intersectionCount++;
            }
        }

        return intersectionCount % 2 == 1;
    }

    public double getHeight() {
        return ceil - floor;
    }
}
