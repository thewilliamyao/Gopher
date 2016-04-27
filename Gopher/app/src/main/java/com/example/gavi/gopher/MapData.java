package com.example.gavi.gopher;

import java.util.ArrayList;

/**
 * Created by grawson2 on 4/27/16.
 */
public class MapData {

    public class Coordinate {

        public double xCoordinate;
        public double yCoordinate;
        public String title;

        public Coordinate(double x, double y, String t) {
            xCoordinate = x;
            yCoordinate = y;
            title = t;
        }

    }

    public ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>() {{
        this.add(new Coordinate(-33.868, 151.210, "Gavi"));
        this.add(new Coordinate(-33.869, 151.201, "William"));
        this.add(new Coordinate(-33.874, 151.206, "Jiayao"));
        this.add(new Coordinate(-33.884, 151.2014, "Suyi"));
    }};

}
