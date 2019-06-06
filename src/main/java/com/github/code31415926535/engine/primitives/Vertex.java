package com.github.code31415926535.engine.primitives;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Vertex {
    @Getter
    private final double x;
    @Getter
    private final double y;

    public Vertex moveByDistanceAndAngle(double distance, double angle) {
        return new Vertex(
                x + distance*Math.cos(angle),
                y + distance*Math.sin(angle)
        );
    }

    public Segment perpendicularLine(double angle) {
        return new Segment(
                this.moveByDistanceAndAngle(GeomUtils.INFINITE, angle+Math.PI / 2),
                this.moveByDistanceAndAngle(-GeomUtils.INFINITE, angle+Math.PI / 2)
        );
    }

    public double distanceTo(Vertex v) {
        return Math.sqrt((x-v.x)*(x-v.x) + (y-v.y)*(y-v.y));
    }

    public double slopeWith(Vertex v) {
        return Math.atan2(y - v.y, x - v.x);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Vertex)) {
            return false;
        }
        Vertex v = (Vertex)o;
        return v.x == x && v.y == y;
    }

    boolean isInsideTriangle(Vertex v1, Vertex v2, Vertex v3) {
        double d1 = GeomUtils.sign(this, v1, v2);
        double d2 = GeomUtils.sign(this, v2, v3);
        double d3 = GeomUtils.sign(this, v3, v1);

        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(hasNeg && hasPos);
    }

    public int getRoundedX() {
        return (int) Math.round(x);
    }

    public int getRoundedY() {
        return (int) Math.round(y);
    }

    public String toString() {
        return String.format("(%1.3f,%1.3f)", x, y);
    }
}