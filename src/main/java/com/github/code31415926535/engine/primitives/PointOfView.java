package com.github.code31415926535.engine.primitives;

import lombok.Getter;

public class PointOfView {
    @Getter
    private Vertex point;
    @Getter
    private double angleOfView;
    @Getter
    private Sector currentSector;

    // TODO: Make this configurable
    @Getter
    private final double fieldOfView = Math.toRadians(60);

    private final double VIEW_CHANGE = Math.toRadians(5);

    public PointOfView(Vertex point, double angleOfView, Sector sector) {
        this.point = point;
        this.angleOfView = angleOfView;

        this.currentSector = sector;
    }

    public void update(Action action) {
        Vertex newPov = point;
        switch (action) {
            case MOVE_FORWARD:
                newPov = point.moveByDistanceAndAngle(1, angleOfView);
                break;
            case MOVE_BACKWARD:
                newPov = point.moveByDistanceAndAngle(-1, angleOfView);
                break;
            case ROTATE_LEFT:
                angleOfView -= VIEW_CHANGE;
                return;
            case ROTATE_RIGHT:
                angleOfView += VIEW_CHANGE;
                return;
        }

        boolean isInside = currentSector.isVertexInside(newPov);
        if (!isInside) {
            for (Segment segment: currentSector.getSegments()) {
                Sector neighbour = segment.getNeighbour();
                if (neighbour != null && neighbour.isVertexInside(newPov)) {
                    this.currentSector = neighbour;
                    point = newPov;
                    return;
                }
            }
            return;
        }

        point = newPov;
    }

    public Vertex getFOVLeftMargin() {
        return point.moveByDistanceAndAngle(GeomUtils.INFINITE, angleOfView + fieldOfView/2);
    }

    public Vertex getFOVRightMargin() {
        return point.moveByDistanceAndAngle(GeomUtils.INFINITE, angleOfView - fieldOfView/2);
    }
}
