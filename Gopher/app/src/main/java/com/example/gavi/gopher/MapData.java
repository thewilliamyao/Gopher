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
        public String address;
        public int image;

        public Coordinate(double x, double y, String t, String a, int i) {
            xCoordinate = x;
            yCoordinate = y;
            title = t;
            address = a;
            image = i;
        }

    }

    public ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>() {{
        this.add(new Coordinate(-33.868, 151.210, "Margarita Pizza", "3700 North Charles St. Baltimore, MD", R.drawable.pizza));
        this.add(new Coordinate(-33.869, 151.201, "Chicken Parmesan", "3100 North Charles St. Baltimore, MD", R.drawable.chicken));
        this.add(new Coordinate(-33.874, 151.206, "Chicken Stir Fry", "3795 North Charles St. Baltimore, MD", R.drawable.stir));
        this.add(new Coordinate(-33.884, 151.2014, "Fettuccine Alfredo", "3705 North Charles St. Baltimore, MD", R.drawable.fetuccine));
    }};

}
