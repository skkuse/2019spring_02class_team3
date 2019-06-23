package edu.skku.swe042_team03.mysecretdairy;

import java.util.HashMap;
import java.util.Map;
//implemented by 이창원 (데이터베이스와 통신하는 단위)
public class DailyRecord {
    public String subheading;//부제목
    public String textdiary;//일기 내용
    public String countryNow;//현재 있는 국가
    public String cityNow;//현재 있는 도시
    public String weatherNow;//현재의 날씨
    public String degreeNow;//현재의 온도
    public String sentiment_score; //감정분석
    public String E_1st,E_2nd,E_3rd,E_4th; //엔티티
    //public ArrayList<String> photodiary;//추후 이미지 파일

    public DailyRecord () { }

    public DailyRecord(String subheading, String textdiary, String countryNow, String cityNow, String weatherNow, String degreeNow,
                       String sentiment_score, String E_1st,String E_2nd,String E_3rd,String E_4th) {
        this.subheading = subheading;
        this.textdiary = textdiary;
        this.countryNow = countryNow;
        this.cityNow = cityNow;
        this.weatherNow = weatherNow;
        this.degreeNow = degreeNow;
        this.sentiment_score = sentiment_score;
        this.E_1st = E_1st;
        this.E_2nd = E_2nd;
        this.E_3rd = E_3rd;
        this.E_4th = E_4th;
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
        result.put("sentiment_score",sentiment_score);
        result.put("E_1st",E_1st);
        result.put("E_2nd",E_2nd);
        result.put("E_3rd",E_3rd);
        result.put("E_4th",E_4th);
        //result.put("photodiary", photodiary);//추후 이미지 파일
        return result;
    }
}