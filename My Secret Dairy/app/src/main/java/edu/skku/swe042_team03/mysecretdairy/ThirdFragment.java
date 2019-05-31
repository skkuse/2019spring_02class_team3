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

// Show March Calendar
// All of code are similar structure with FirstFragment.java

public class ThirdFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.month3, container, false);
        getYear = calendarView.year;
        getMonth = calendarView.month;
        getDay = calendarView.day;
        cal.set(getYear, Calendar.MARCH, 1);
        dayofmonth = cal.getActualMaximum(Calendar.DATE);
        materialcalendarview = view.findViewById(R.id.calendar3);
        materialcalendarview.setTopbarVisible(false);
        editText = view.findViewById(R.id.edit);
        editText.setText(getYear + " " + getMonth + " " + getDay);
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
                        .setMinimumDate(CalendarDay.from(getYear, 3,1))
                        .setMaximumDate(CalendarDay.from(getYear, 3, dayofmonth))
                        .commit();
            }
        });
        return view;
    }
}
