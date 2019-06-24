package edu.skku.swe042_team03.mysecretdairy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//implemented by 양희산
public class MySecretChart extends AppCompatActivity {

    private LineChart lineChart;

    List<Entry> entries = new ArrayList<>();

    private DatabaseReference mPostReference;
    int cal_year;   // 캘린더 날짜 받을 변수
    int cal_month;   // 캘린더 날짜 받을 변수
    int cal_day;   // 캘린더 날짜 받을 변수
    String id = "";         // id 받을 변수
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        lineChart = (LineChart)findViewById(R.id.chart);


        mPostReference = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        id = intent.getExtras().getString("id");

        getFirebaseDatabase();

    }

    public void getFirebaseDatabase() { //Record를 검색하여 sentiment_score값을 읽어내는 함수
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int num = 1;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot secondSnapshot : postSnapshot.getChildren())
                    {
                        for (DataSnapshot thirdSnapshot : secondSnapshot.getChildren()) {
                            for (DataSnapshot fourthSnapshot : thirdSnapshot.getChildren())
                            {

                                String day = thirdSnapshot.getKey();

                                DailyRecord get = fourthSnapshot.getValue(DailyRecord.class);
                                float info = Float.parseFloat(get.sentiment_score);
                                entries.add(new Entry(Integer.parseInt(day),info)); // 값을 읽어서 ArrayList에 저장
                                num = num+1;
                            }
                        }
                    }
                }
                LineDataSet lineDataSet = new LineDataSet(entries, "Score"); // ArrayList값을 Chart에 그리기 위한 DataSet에 저장
                lineDataSet.setLineWidth(2);
                lineDataSet.setCircleRadius(6);
                lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
                lineDataSet.setCircleColorHole(Color.BLUE);
                lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
                lineDataSet.setDrawCircleHole(true);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setDrawHorizontalHighlightIndicator(false);
                lineDataSet.setDrawHighlightIndicators(false);
                lineDataSet.setDrawValues(false);

                LineData lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.BLACK);
                xAxis.enableGridDashedLine(8, 24, 0);

                YAxis yLAxis = lineChart.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);

                YAxis yRAxis = lineChart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);

                Description description = new Description();
                description.setText("");

                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.setDescription(description);
                lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
                lineChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/" + id).child("/").addValueEventListener(postListener);


    }
}
