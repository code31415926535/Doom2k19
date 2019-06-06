package com.github.code31415926535.engine.primitives;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Segment {
    private Vertex a;
    private Vertex b;

    @Getter
    @Setter
    private Sector neighbour;

    public Segment(Vertex a, Vertex b) {
        this.a = a;
        this.b = b;
        neighbour = null;
    }

    public Segment getFieldOfViewIntersection(Vertex origin, Vertex s, Vertex e) {
        // The origin point and 2 field of view points (far away) create a triangle.
        // What is inside this triangle is visible.
        boolean isAInsideTriangle = a.isInsideTriangle(origin, s, e);
        boolean isBInsideTriangle = b.isInsideTriangle(origin, s, e);

        // If both points are inside the triangle, the whole segment is visible.
        if (isAInsideTriangle && isBInsideTriangle) {
            return new Segment(a, b);
        }

        Vertex iS = GeomUtils.intersection(origin, s, a, b);
        Vertex iE = GeomUtils.intersection(origin, e, a, b);

        // If a is inside and b is not, there is an intersection
        // between (a,b) and either (origin,s) or (origin,e)
        // [a,intersection] is the result.
        if (isAInsideTriangle) {
            if (isOnSegment(iS)) {
                return new Segment(a, iS);
            } else {
                return new Segment(a, iE);
            }
        }

        // Same as above.
        if (isBInsideTriangle) {
            if (isOnSegment(iS)) {
                return new Segment(b, iS);
            } else {
                return new Segment(b, iE);
            }
        }

        Segment startSeg = new Segment(origin, s);
        // if there are 2 intersections it means the segment covers
        // the whole field of view. The last check makes sure it's
        // in front and not behind.
        if (isOnSegment(iS) && isOnSegment(iE) && startSeg.isOnSegment(iS)) {
            return new Segment(iS, iE);
        }

        return null;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Segment)) {
            return false;
        }
        Segment s = (Segment)o;
        return (s.a.equals(a) && s.b.equals(b)) || (s.a.equals(b) && s.b.equals(a));
    }

    public boolean isOnSegment(Vertex v) {
        double distanceVA = a.distanceTo(v);
        double distanceVB = b.distanceTo(v);
        double distanceAB = a.distanceTo(b);

        // use margin of error.
        return distanceVA + distanceVB - distanceAB < 0.000001;
    }

    public String toString() {
        return "[" + a + "-" + b + "]";
    }
}
