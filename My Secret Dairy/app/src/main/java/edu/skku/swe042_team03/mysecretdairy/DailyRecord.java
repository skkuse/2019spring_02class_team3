package edu.skku.swe042_team03.mysecretdairy;

import java.util.HashMap;
import java.util.Map;

public class DailyRecord {
    public String subheading;
    public String textdiary;
    public String countryNow;
    public String cityNow;
    public String weatherNow;
    public String degreeNow;
    //public ArrayList<String> photodiary;//추후 이미지 파일

    public DailyRecord () { }

    public DailyRecord(String subheading, String textdiary, String countryNow, String cityNow, String weatherNow, String degreeNow) {
        this.subheading = subheading;
        this.textdiary = textdiary;
        this.countryNow = countryNow;
        this.cityNow = cityNow;
        this.weatherNow = weatherNow;
        this.degreeNow = degreeNow;
        //this.photodiary = photodiary;//추후 이미지 파일
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("subheading", subheading);
        result.put("textdiary", textdiary);
        result.put("countryNow", countryNow);
        result.put("cityNow", cityNow);
        result.put("weatherNow", weatherNow);
        result.put("degreeNow", degreeNow);
        //result.put("photodiary", photodiary);//추후 이미지 파일
        return result;
    }
}