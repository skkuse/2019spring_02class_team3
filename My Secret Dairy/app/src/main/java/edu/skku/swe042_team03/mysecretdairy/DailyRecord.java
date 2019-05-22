package edu.skku.swe042_team03.mysecretdairy;

import java.util.HashMap;
import java.util.Map;

public class DailyRecord {
    public String subheading;
    public String textdiary;
    //public ArrayList<String> photodiary;//추후 이미지 파일

    public DailyRecord () { }

    public DailyRecord(String subheading, String textdiary) {
        this.subheading = subheading;
        this.textdiary = textdiary;
        //this.photodiary = photodiary;//추후 이미지 파일
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("subheading", subheading);
        result.put("textdiary", textdiary);
        //result.put("photodiary", photodiary);//추후 이미지 파일
        return result;
    }
}