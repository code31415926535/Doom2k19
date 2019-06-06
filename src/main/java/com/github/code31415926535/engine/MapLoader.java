package com.github.code31415926535.engine;

import com.github.code31415926535.engine.exceptions.InvalidMapFormatException;
import com.github.code31415926535.engine.primitives.Sector;
import com.github.code31415926535.engine.primitives.Vertex;
import com.github.code31415926535.engine.primitives.Segment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MapLoader {
    private static final String MAP_DIR_ROOT = "maps";

    private static final String COMMENT_PREFIX = "#";
    private static final String SECTOR_PREFIX = "sector";
    private static final String BOUND_TWO_WAY_PREFIX = "bound2";
    private static final String PLAYER_PREFIX = "player";

    private String filename;

    private HashMap<String, Sector> sectors;
    private String playerSector;
    private Vertex playerVertex;
    private double playerAngle;

    public MapLoader(String filename) throws NullPointerException {
        this.filename = getClass().getClassLoader().getResource(MAP_DIR_ROOT + "/" + filename).getFile();
    }

    public Map load() throws FileNotFoundException, InvalidMapFormatException {
        File file = new File(filename);
        Scanner sc = new Scanner(file);

        sectors = new HashMap<>();
        playerSector = null;
        playerVertex = null;
        playerAngle = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            // Skip empty lines and comments.
            if (isCommentLine(line) || isEmptyLine(line)) {
                continue;
            }

            String[] split = line.split(" ");
            if (split.length < 1) {
                throw new InvalidMapFormatException("line is invalid (" + line + "_)");
            }

            // Other lines must be valid
            switch (split[0]) {
                case SECTOR_PREFIX:
                    loadSector(split);
                    break;
                case BOUND_TWO_WAY_PREFIX:
                    loadTwoWayBound(split);
                    break;
                case PLAYER_PREFIX:
                    loadPlayer(split);
                    break;
                default:
                    throw new InvalidMapFormatException("unknown type (" + split[0] + ")");
            }
        }

        if (playerSector == null) {
            throw new InvalidMapFormatException("map does not contain player");
        }

        return new DefaultMap(sectors, playerVertex, playerAngle, playerSector);
    }

    private void loadSector(String[] split) throws InvalidMapFormatException {
        // Minimal valid sector
        // sector <name> <floor> <ceil> <x> <y> <z>
        if (split.length < 6) {
            throw new InvalidMapFormatException("sector has to have at least 6 entries");
        }
        double floor = Double.parseDouble(split[2]);
        double ceil = Double.parseDouble(split[3]);

        List<Segment> segments = new ArrayList<>();

        for (int i = 5; i < split.length; i++) {
            Vertex x = loadVertex(split[i-1]);
            Vertex y = loadVertex(split[i]);
            segments.add(new Segment(x, y));
        }

        Vertex start = loadVertex(split[4]);
        Vertex end = loadVertex(split[split.length-1]);
        segments.add(new Segment(end, start));

        String name = getSectorName(split);
        Sector sector = new Sector(name, floor, ceil, segments);
        sectors.put(name, sector);
    }

    private void loadTwoWayBound(String[] split) throws InvalidMapFormatException {
        if (split.length != 5) {
            throw new InvalidMapFormatException("bound has to have exactly 5 entries");
        }

        String nameX = split[1];
        Sector x = sectors.get(nameX);
        String nameY = split[2];
        Sector y = sectors.get(nameY);

        Vertex a = loadVertex(split[3]);
        Vertex b = loadVertex(split[4]);
        Segment segment = new Segment(a, b);

        x.addNeighbour(segment, y);
        y.addNeighbour(segment, x);
    }

    private void loadPlayer(String[] split) throws InvalidMapFormatException {
        if (split.length != 4) {
            throw new InvalidMapFormatException("player has to have exactly 4 entries");
        }
        playerSector = getSectorName(split);
        playerVertex = loadVertex(split[2]);
        playerAngle = Double.parseDouble(split[3]);
    }

    private Vertex loadVertex(String data) throws InvalidMapFormatException {
        String[] points = data.replace("(", "").replace(")", "").split(",");
        if (points.length != 2) {
            throw new InvalidMapFormatException("vertex needs to be of format (x,y)");
        }
        return new Vertex(
                Double.parseDouble(points[0]),
                Double.parseDouble(points[1])
        );
    }

    private String getSectorName(String[] split) {
        return split[1];
    }

    private boolean isCommentLine(String line) {
        return line.startsWith(COMMENT_PREFIX);
    }

    private boolean isEmptyLine(String line) {
        return line.trim().length() == 0;
    }
}
