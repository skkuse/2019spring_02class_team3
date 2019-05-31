package edu.skku.swe042_team03.mysecretdairy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

// Show January Calendar

public class FirstFragment extends Fragment {
    final static CalendarView calendarView = new CalendarView();
    private MaterialCalendarView materialcalendarview;
    private static int getYear;
    private static int getMonth;
    private static int getDay;
    private int dayofmonth;
    EditText editText;
    Calendar cal = Calendar.getInstance();
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month1, container, false);
        // get Date from CalendarView Activity
        getYear = calendarView.year;
        getMonth = calendarView.month;
        getDay = calendarView.day;
        // calendar set getyear/1/1
        cal.set(getYear, Calendar.JANUARY, 1);
        // get maximumday of calendar(getyear/1/1)
        dayofmonth = cal.getActualMaximum(Calendar.DATE);
        materialcalendarview = view.findViewById(R.id.calendar1);
        materialcalendarview.setTopbarVisible(false);
        // set text for trigger material calendar setting changed
        editText = view.findViewById(R.id.edit);
        editText.setText(getYear + " " + getMonth + " " + getDay);
        // material calendar setting at first
        materialcalendarview.state().edit()
                .setMinimumDate(CalendarDay.from(getYear, 1,1))
                .setMaximumDate(CalendarDay.from(getYear, 1, dayofmonth))
                .commit();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                materialcalendarview.state().edit()
                        .setMinimumDate(CalendarDay.from(getYear, 1,1))
                        .setMaximumDate(CalendarDay.from(getYear, 1, dayofmonth))
                        .commit();
            }
        });
        return view;
    }
}

