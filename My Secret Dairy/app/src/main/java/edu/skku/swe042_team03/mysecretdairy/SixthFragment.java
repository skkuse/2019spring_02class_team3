package edu.skku.swe042_team03.mysecretdairy;
//코드 by 신현호
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import java.util.Calendar;

// Show June Calendar
// All of code are similar structure with FirstFragment.java

public class SixthFragment extends Fragment {
    final static CalendarView calendarView = new CalendarView();
    private MaterialCalendarView materialcalendarview;
    private static int getYear;
    private static int getMonth;
    private static int getDay;
    private int dayofmonth;
    EditText editText;
    Calendar cal = Calendar.getInstance();
    Intent intent;
    private int cal_year;
    private int cal_month;
    private int cal_day;
    private static String getId;
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month6, container, false);
        getYear = calendarView.year;
        getMonth = calendarView.month;
        getDay = calendarView.day;
        getId = calendarView.id;
        cal.set(getYear, Calendar.JUNE, 1);
        dayofmonth = cal.getActualMaximum(Calendar.DATE);
        materialcalendarview = view.findViewById(R.id.calendar6);
        materialcalendarview.setTopbarVisible(false);
        editText = view.findViewById(R.id.edit);
        editText.setText(getYear + " " + getMonth + " " + getDay);
        materialcalendarview.state().edit()
                .setMinimumDate(CalendarDay.from(getYear, 6,1))
                .setMaximumDate(CalendarDay.from(getYear, 6, dayofmonth))
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
                        .setMinimumDate(CalendarDay.from(getYear, 6,1))
                        .setMaximumDate(CalendarDay.from(getYear, 6, dayofmonth))
                        .commit();
                materialcalendarview.setDateSelected(CalendarDay.from(getYear,getMonth+1,getDay), true);
            }
        });
        materialcalendarview.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay) {
                materialCalendarView.clearSelection();
                cal_year = calendarDay.getYear();
                cal_month = calendarDay.getMonth();
                cal_day = calendarDay.getDay();
                intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("cal_year",cal_year);
                intent.putExtra("cal_month",cal_month);
                intent.putExtra("cal_day",cal_day);
                intent.putExtra("id",getId);
                startActivity(intent);
            }
        });
        return view;
    }
}
