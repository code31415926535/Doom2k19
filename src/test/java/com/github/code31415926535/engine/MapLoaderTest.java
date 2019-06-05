package com.github.code31415926535.engine;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class MapLoaderTest {

    @Test(expected = NullPointerException.class)
    public void loadMap_throwsExceptionOnFileNotFound() throws FileNotFoundException {
        new MapLoader("asdf.txt").load();
    }

    @Test
    public void loadMap_canLoadSimpleMap() throws FileNotFoundException {
        Map map = new MapLoader("simple.txt").load();
        assertEquals(map.sectors.size(), 1);
        assertEquals(map.povSector, "c");
    }
}
