package edu.skku.swe042_team03.mysecretdairy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest; // 엔티티위해 추가
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse; //엔티티위해 추가
import com.google.cloud.language.v1.Entity; //엔티티위해추가
import com.google.cloud.language.v1.EntityMention; //엔티티위해추가
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
import com.google.auth.oauth2.GoogleCredentials; //감정분석위해 추가
import com.google.cloud.language.v1.Document; //감정분석위해 추가
import com.google.cloud.language.v1.LanguageServiceClient; //감정분석위해 추가
import com.google.cloud.language.v1.LanguageServiceSettings; //감정분석위해 추가
import com.google.cloud.language.v1.Sentiment; //감정분석위해 추가
import java.io.IOException; //감정분석위해 추가
//implemented by 이창원&신현호&차미경
public class MainActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    StorageReference ref;
    ImageView[] imageViews = new ImageView[100];
    ImageView imageView1;
    Intent intent;
    String day = "", subheading="", textdiary = "";
    String textdiary2 = ""; //감정분석위해추가
    String textdiary3 = ""; //엔티티위해추가
    ArrayList<String> photodiaty;
    EditText editText1;
    EditText editText2;
    TextView textView1;
    TextView textView2;
    TextView textView;//감정분석 출력값
    TextView text_entity1; //해시태그 부분
    TextView text_entity2; //해시태그 부분
    TextView text_entity3; //해시태그 부분
    TextView text_entity4; //해시태그 부분
    String countryNow = "";
    String cityNow = "";
    String weatherNow = "";
    String degreeNow = "";
    Double longitude = 0.0;
    Double latitude = 0.0;
    Weatheritem parse_data;
    String sentiment_score; //감정분석 score값 저장위해

    ImageView imageView;    //이미지
    Uri filepath;           //이미지 경로
    String imgName;         //이미지 이름

    private LanguageServiceClient mLanguageClient; //감정분석위해 추가
    private Document document2; //감정분석위해 추가
    private Document document3;//엔티티위해 추가
    private Sentiment sentiment; //감정분석위해 추가

    ArrayList<String> forTag = new ArrayList<String>(); //해시태그
    float max_1st, max_2nd, max_3rd, max_4th; //해시태그
    String E_1st="", E_2nd="", E_3rd="", E_4th=""; //해시태그

    int cal_year;   // 캘린더 날짜 받을 변수
    int cal_month;   // 캘린더 날짜 받을 변수
    int cal_day;   // 캘린더 날짜 받을 변수
    String id = "";         // id 받을 변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button)(findViewById(R.id.button1));
        editText1 = (EditText)(findViewById(R.id.editText1));
        editText2 = (EditText)(findViewById(R.id.editText2));
        textView1 = (TextView)(findViewById(R.id.textView1));
        textView = (TextView)(findViewById(R.id.textView)); // 감정분석 text
        Button addPhoto = (Button)(findViewById(R.id.button4)) ;
        Button savePhoto = findViewById(R.id.button5);
        Button getlocation = (Button)(findViewById(R.id.button6));
        imageView = findViewById(R.id.imageView);
        mPostReference = FirebaseDatabase.getInstance().getReference();

        intent = getIntent();
        id = intent.getExtras().getString("id");                // id 받기
        cal_year = intent.getExtras().getInt("cal_year");    // 캘린더 날짜 받기
        cal_month = intent.getExtras().getInt("cal_month");   // 캘린더 날짜 받기
        cal_day = intent.getExtras().getInt("cal_day");    // 캘린더 날짜 받기
        imgName = id+"_"+cal_year+cal_month+cal_day;
        textView1.setText(cal_year + "/" + cal_month + "/" + cal_day);
        ref = FirebaseStorage.getInstance().getReference("images/" + imgName + ".png");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("cal_year",cal_year);
                intent.putExtra("cal_month",cal_month);
                intent.putExtra("cal_day",cal_day);
                intent.putExtra("id",id);
                subheading = editText1.getText().toString();
                textdiary = editText2.getText().toString();
                day = textView1.getText().toString();
                if ((subheading.length() + day.length() +textdiary.length()) < 5) {
                    Toast.makeText(MainActivity.this, "공란을 채워주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabase(true);
                    startActivity(intent);
                    finish();//view가 쌓이는 것을 막기 위해서 finish를 사용하여 종료시켜줌.
                }
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "이미지를 선택하세요."), 1);
            }
        });

        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        getFirebaseDatabase();
        Glide.with(this).load(ref).centerCrop().transition(DrawableTransitionOptions.withCrossFade()).into(imageView);

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
        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, gpsLocationListener);//GPS를 통한 경우보다 NETWORK를 통한 위치정보가 더 정확하여, NETWORK를 사용합니다.
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, networkLocationListener);

        MyAsyncTask mProcessTask = new MyAsyncTask();
        mProcessTask.execute();

        //아래 try 감정분석위해 추가
        // create the language client
        try {
            // NOTE: The line below uses an embedded credential (res/raw/credential.json).
            //       You should not package a credential with real application.
            //       Instead, you should get a credential securely from a server.
            mLanguageClient = LanguageServiceClient.create(
                    LanguageServiceSettings.newBuilder()
                            .setCredentialsProvider(() ->
                                    GoogleCredentials.fromStream(getApplicationContext()
                                            .getResources()
                                            .openRawResource(R.raw.credential)))
                            .build());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create a language client", e);
        }
    }

    // 데이뷰에 이미지 넣기
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        //request코드가 1이고 OK를 선택했고 data에 뭔가가 들어있다면
        if(requestCode == 1 && resultCode == RESULT_OK) {
            try {
                // 선택한 이미지에서 비트맵 생성
                imageView = findViewById(R.id.imageView);
                filepath = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                // 이미지 표시
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //upload img file
    private void uploadFile() {
        // 업로드할 파일이 있으면 수행
        if(filepath != null) {
            // 업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //파일명 지정
            String filename = imgName + ".png";

            //storage 주소와 폴더 파일명 지정
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + filename);

            mStorageRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                //성공시
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss(); //업로드 진행상자 닫기
                    Toast.makeText(getApplicationContext(),"업로드 완료!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                //실패시
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                //진행중
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    //dialog에 진행률을 퍼센트로 출력
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                }
            });
        } else {

        }
    }

    //아래 public void onSentimetClicked(View view) : 감정분석위해 추가
    public void onSentimentClicked(View view){
        textdiary2 = editText2.getText().toString();
        document2 = Document.newBuilder().setContent(textdiary2).setType(Document.Type.PLAIN_TEXT).build(); //감정분석위해 추가
        sentiment = mLanguageClient.analyzeSentiment(document2).getDocumentSentiment();//감정분석위해 추가
        Toast.makeText(getApplicationContext(), String.valueOf(sentiment.getScore()), Toast.LENGTH_LONG).show();
        sentiment_score = String.valueOf(sentiment.getScore()); //감정분석 score값 저장위해

        textView.setText("Score: " +String.valueOf(sentiment.getScore())+"\nMagnitude: " +String.valueOf(sentiment.getMagnitude()) );
    }

    //아래 public void on EntityClicked(View view) : 엔티티위해 추가
    public void onEntityClicked(View view) {
        textdiary3 = editText2.getText().toString();
        document3 = Document.newBuilder().setContent(textdiary3).setType(Document.Type.PLAIN_TEXT).build();
        AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
                .setDocument(document3)
                .build();
        AnalyzeEntitySentimentResponse response = mLanguageClient.analyzeEntitySentiment(request);
        // Print the response
        max_1st = 0;max_2nd=0;max_3rd=0;max_4th=0;
        E_1st = "";E_2nd = "";E_3rd = "";E_4th = "";
        for (Entity entity : response.getEntitiesList()) {
            for (EntityMention mention : entity.getMentionsList()) {
                System.out.printf("Content: %s\n", mention.getText().getContent());
                System.out.printf("Magnitude: %.3f\n", mention.getSentiment().getMagnitude());
                System.out.printf("Sentiment score : %.3f\n", mention.getSentiment().getScore());
                System.out.printf("Type: %s\n\n", mention.getType());

                float value = mention.getSentiment().getMagnitude() * mention.getSentiment().getScore();
                float mid1,mid2;
                String mid1_S,mid2_S;
                int det=0;
                if(max_1st < value){
                    det =1;
                    mid1 = max_1st;
                    mid1_S = E_1st;
                    max_1st = value;
                    E_1st = mention.getText().getContent();

                    //뒤로 미는 작업
                    mid2 = max_2nd;
                    mid2_S = E_2nd;
                    max_2nd = mid1; //원래 max_1st가 max_2nd가 된다.
                    E_2nd = mid1_S;
                    mid1 = max_3rd;
                    mid1_S = E_3rd;
                    max_3rd = mid2; //원래 max_2nd가 max_3rd가 된다.
                    E_3rd = mid2_S;
                    max_4th = mid1; //원래 max_3rd가 max_4th가 된다.
                    E_4th = mid1_S;
                }
                else if(det==0 && max_2nd < value){
                    det = 1;
                    mid1 = max_2nd;
                    mid1_S = E_2nd;
                    max_2nd = value;
                    E_2nd = mention.getText().getContent();

                    //뒤로 미는 작업
                    mid2 = max_3rd;
                    mid2_S = E_3rd;
                    max_3rd = mid1; //원래 max_2nd가 max_3rd가 된다.
                    E_3rd = mid1_S;
                    max_4th = mid2; //원래 max_3rd가 max_4th가 된다.
                    E_4th = mid2_S;
                }
                else if(det==0 && max_3rd < value){
                    det =1;
                    mid1 = max_3rd;
                    mid1_S = E_3rd;
                    max_3rd = value;
                    E_3rd = mention.getText().getContent();

                    //뒤로 미는 작업
                    max_4th = mid1;//원래 max_3rd가 max_4th가 된다.
                    E_4th = mid1_S;
                }
                else if(det==0 && max_4th < value){
                    max_4th = value;
                    E_4th = mention.getText().getContent();
                }
            }
        }

        if(E_1st != ""){
            forTag.add(E_1st);
            System.out.printf("E_1st: %s\n",E_1st);
        }
        if(E_2nd !=""){
            forTag.add(E_2nd);
            System.out.printf("E_2nd: %s\n",E_2nd);
        }
        if(E_3rd != ""){
            forTag.add(E_3rd);
            System.out.printf("E_3rd: %s\n",E_3rd);
        }
        if(E_4th != "") {
            forTag.add(E_4th);
            System.out.printf("E_4th: %s\n", E_4th);
        }

        TextView text_e1 = (TextView)findViewById(R.id.text_entity1);
        TextView text_e2 = (TextView)findViewById(R.id.text_entity2);
        TextView text_e3 = (TextView)findViewById(R.id.text_entity3);
        TextView text_e4 = (TextView)findViewById(R.id.text_entity4);
        if(E_1st != "") {
            text_e1.setText("#" + E_1st);
        }
        if(E_2nd != "") {
            text_e2.setText("#" + E_2nd);
        }
        if(E_3rd != "") {
            text_e3.setText("#" + E_3rd);

        }
        if(E_4th != "") {
            text_e4.setText("#" + E_4th);
        }
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    DailyRecord get = postSnapshot.getValue(DailyRecord.class);
                    String[] info = {get.subheading, get.textdiary, get.countryNow, get.cityNow, get.weatherNow,get.degreeNow
                            , String.valueOf(get.sentiment_score),get.E_1st,get.E_2nd,get.E_3rd,get.E_4th};
                    countryNow = get.countryNow;
                    cityNow = get.cityNow;
                    weatherNow = get.weatherNow;
                    degreeNow = get.degreeNow;
                    sentiment_score = get.sentiment_score;
                    E_1st = get.E_1st;
                    E_2nd = get.E_2nd;
                    E_3rd = get.E_3rd;
                    E_4th = get.E_4th;
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3] + info[4] + info[5]+
                            info[6]+info[7]+info[8]+info[9]+info[10]);
                    editText1 = (EditText)(findViewById(R.id.editText1));
                    editText2 = (EditText)(findViewById(R.id.editText2));
                    textView2 = (TextView) findViewById(R.id.textView2);
                    textView = (TextView) findViewById(R.id.textView);
                    text_entity1 = (TextView) findViewById(R.id.text_entity1);
                    text_entity2 = (TextView) findViewById(R.id.text_entity2);
                    text_entity3 = (TextView) findViewById(R.id.text_entity3);
                    text_entity4 = (TextView) findViewById(R.id.text_entity4);
                    editText1.setText(info[0]);
                    editText2.setText(info[1]);
                    textView2.setText(" " + info[2] + " " + info[3] + "\n" + " " + info[4] + " " + info[5]);
                    textView.setText("Sentiment\nScore: "+info[6]);
                    if(E_1st!="") {
                        text_entity1.setText("#" + info[7]);
                    }
                    if(E_2nd!="") {
                        text_entity2.setText("#" + info[8]);
                    }
                    if(E_3rd!="") {
                        text_entity3.setText("#" + info[9]);
                    }
                    if(E_4th!="") {
                        text_entity4.setText("#" + info[10]);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("/" + id).child("/" + cal_year).child("/" + cal_month).child("/" + cal_day).child("/").addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            DailyRecord post = new DailyRecord(subheading, textdiary, countryNow, cityNow, weatherNow, degreeNow,
                    sentiment_score,E_1st,E_2nd,E_3rd,E_4th);//추후 이미지 파일 연결
            postValues = post.toMap();
        }
        childUpdates.put("/" + id + "/" + day + "/record", postValues);
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
