package edu.skku.swe042_team03.mysecretdairy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class CalendarView extends AppCompatActivity {
    Intent intent;
    ImageView calendar_btn;
    TextView calendar_year;
    FragmentPagerAdapter adapterViewPager;
    DatePickerDialog.OnDateSetListener setListener;
    public static int year;
    public static int month;
    public static int day;
    public static String id = "developer";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main);
        // Using DatePickerDialog for set date
        calendar_year = findViewById(R.id.tv_calendar_year);
        calendar_btn = findViewById(R.id.iv_calendar_btn);
        Calendar cal = Calendar.getInstance();
        final Calendar minDate = Calendar.getInstance();
        final Calendar maxDate = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        // Using viewpager for swipe motion
        final ViewPager vpPager = (ViewPager) findViewById(R.id.vp);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOffscreenPageLimit(3);
        vpPager.setClipToPadding(false);
        vpPager.setPageMargin(80);
        vpPager.setCurrentItem(month);

        // 로그인 아이디 받기
        intent = getIntent();
        id = intent.getExtras().getString("id");

        calendar_btn.setOnClickListener(new View.OnClickListener() {                        // Call calendar dialog
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CalendarView.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.setTitle("Select a Date");
                minDate.set(2010,0,1);                                  // Minimum date 2010/1/1
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
                maxDate.set(2029, 11,31);                               // Maximum date 2029/12/31
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

            setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int select_year, int select_month, int select_day) {
                day = select_day;
                month = select_month;
                year = select_year;
                calendar_year.setText(year + "");
                vpPager.setCurrentItem(month);
                adapterViewPager.notifyDataSetChanged();
            }
        };
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 12;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                case 3:
                    return new FourthFragment();
                case 4:
                    return new FifthFragment();
                case 5:
                    return new SixthFragment();
                case 6:
                    return new SeventhFragment();
                case 7:
                    return new EighthFragment();
                case 8:
                    return new NinethFragment();
                case 9:
                    return new TenthFragment();
                case 10:
                    return new EleventhFragment();
                case 11:
                    return new TwelfthFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
