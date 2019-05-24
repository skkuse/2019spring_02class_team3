package edu.skku.swe042_team03.mysecretdairy;

public class Weatheritem {
    public Coord coord;
    public Weather[] weather;
    public Main main;
    public Wind wind;
    public Sys sys;
    public String name;

    public class Coord{
        public float lon;
        public float lat;
    }

    public class Weather{
        public String main;
        public String description;
        public String icon;
    }

    public class Main{
        public float temp;
        public float humidity;
    }

    public class Wind{
        public String speed;
        public String deg;
    }

    public class Sys{
        public String country;
    }

}
