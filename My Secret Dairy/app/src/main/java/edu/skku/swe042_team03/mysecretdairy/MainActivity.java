package edu.skku.swe042_team03.mysecretdairy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    private StorageReference mStorageRef;
    ImageView[] imageViews = new ImageView[100];
    ImageView imageView1;
    Intent intent;
    String day = "", subheading="", textdiary = "";
    ArrayList<String> photodiaty;
    EditText editText1;
    EditText editText2;
    TextView textView1;
    TextView textView2;
    String countryNow = "";
    String cityNow = "";
    String weatherNow = "";
    String degreeNow = "";
    Double longitude = 0.0;
    Double latitude = 0.0;
    Weatheritem parse_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button)(findViewById(R.id.button1));
        editText1 = (EditText)(findViewById(R.id.editText1));
        editText2 = (EditText)(findViewById(R.id.editText2));
        textView1 = (TextView)(findViewById(R.id.textView1));
        Button addPhoto = (Button)(findViewById(R.id.button4)) ;
        Button getlocation = (Button)(findViewById(R.id.button6));
        mPostReference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CalendarView.class);
                subheading = editText1.getText().toString();
                textdiary = editText2.getText().toString();
                day = textView1.getText().toString();

                if ((subheading.length() + day.length() +textdiary.length()) < 5) {
                    Toast.makeText(MainActivity.this, "공란을 채워주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabase(true);
                    startActivity(intent);
                }
            }
        });

        getFirebaseDatabase();


        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTask mProcessTask = new MyAsyncTask();
                mProcessTask.execute();
                countryNow = parse_data.sys.country;
                cityNow = parse_data.name;
                weatherNow = parse_data.weather[0].main;
                degreeNow = String.format("%.2f", parse_data.main.temp - 273);
                textView2 = (TextView) findViewById(R.id.textView2);
                textView2.setText(" " + countryNow + " " + cityNow + "\n" + " " + weatherNow + " " + degreeNow);
            }
        });

        final LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        else{
//            Toast.makeText(MainActivity.this, "LocationManager is ready!", Toast.LENGTH_SHORT).show();
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                100,
                0,
                gpsLocationListener);
//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                100,
//                0,
//                networkLocationListener);

        MyAsyncTask mProcessTask = new MyAsyncTask();
        mProcessTask.execute();

    }
    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    DailyRecord get = postSnapshot.getValue(DailyRecord.class);
                    String[] info = {get.subheading, get.textdiary, get.countryNow, get.cityNow, get.weatherNow,get.degreeNow };
                    countryNow = get.countryNow;
                    cityNow = get.cityNow;
                    weatherNow = get.weatherNow;
                    degreeNow = get.degreeNow;
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3] + info[4] + info[5]);
                    editText1 = (EditText)(findViewById(R.id.editText1));
                    editText2 = (EditText)(findViewById(R.id.editText2));
                    textView2 = (TextView) findViewById(R.id.textView2);
                    editText1.setText(info[0]);
                    editText2.setText(info[1]);
                    textView2.setText(" " + info[2] + " " + info[3] + "\n" + " " + info[4] + " " + info[5]);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/" ).addValueEventListener(postListener);
    }
    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            DailyRecord post = new DailyRecord(subheading, textdiary, countryNow, cityNow, weatherNow, degreeNow);//추후 이미지 파일 연결
            postValues = post.toMap();
        }
        childUpdates.put("/" + day, postValues);
        mPostReference.updateChildren(childUpdates);
        clearC();
    }

    public void clearC () {
        subheading = "";
        textdiary = "";
        //photodiaty.clear();//추후 이미지 파일 연결
    }
    final LocationListener networkLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d("Update Network", "" + provider + " " + longitude);
//            textView2 = (TextView)(findViewById(R.id.textView2));
//            textView2.setText("ready!");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            Log.d("Update GPS", "" + provider + " " + longitude);
//            textView2 = (TextView)(findViewById(R.id.textView2));
//            textView2.setText("ready!");
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
    };
    public class MyAsyncTask extends AsyncTask<String, Void, Weatheritem> {
        OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Weatheritem doInBackground(String... params) {
            HttpUrl.Builder urlbuilder = HttpUrl.parse("http://api.openweathermap.org/data/2.5/weather?lat="+latitude.toString()+"&lon="+longitude.toString()).newBuilder();
            urlbuilder.addQueryParameter("APPID", "3fafdc3cd1c71546623ddb13935d885f");
            String requestUrl = urlbuilder.build().toString();
            Request request = new Request.Builder().url(requestUrl).build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream()).getAsJsonObject();
                parse_data=gson.fromJson(rootObject,Weatheritem.class);
                return parse_data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Weatheritem result) {
            super.onPostExecute(result);
        }
    }
}
