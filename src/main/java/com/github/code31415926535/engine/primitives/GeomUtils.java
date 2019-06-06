package com.github.code31415926535.engine.primitives;

public final class GeomUtils {
    public static final int INFINITE = 1000;

    private GeomUtils() {}

    private static final double TWO_PI = Math.PI * 2;
    private static double vectorCrossProduct(double x1, double y1, double x2, double y2) {
        return x1*y2 - y1*x2;
    }

    public static Vertex intersection(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        double a = vectorCrossProduct(v1.getX(), v1.getY(), v2.getX(), v2.getY());
        double b = vectorCrossProduct(v3.getX(), v3.getY(), v4.getX(), v4.getY());
        double det = vectorCrossProduct(
                v1.getX() -v2.getX(),
                v1.getY() -v2.getY(),
                v3.getX() -v4.getX(),
                v3.getY() - v4.getY()
        );
        // Prevent division by 0
        if (det == 0) {
            det = 0.00000000001;
        }
        return new Vertex(
                vectorCrossProduct(a, v1.getX() - v2.getX(), b, v3.getX()-v4.getX()) / det,
                vectorCrossProduct(a, v1.getY() - v2.getY(), b, v3.getY() - v4.getY()) / det
        );
    }

    public static double normalizeAngle(double angle) {
        return normalizeAngle(angle, 0);
    }

    public static double normalizeAngle(double angle, double center) {
        return angle - TWO_PI * Math.floor((angle+Math.PI - center) / TWO_PI);
    }

    public static double sign(Vertex v1, Vertex v2, Vertex v3) {
        return (v1.getX() - v3.getX()) * (v2.getY() - v3.getY()) - (v2.getX() - v3.getX()) * (v1.getY()-v3.getY());
    }

    public static int clamp(int x, int start, int end) {
        if (x < start) {
            return start;
        }
        if (x > end) {
            return end;
        }
        return x;
    }
}
