package edu.skku.swe042_team03.mysecretdairy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
//    private StorageReference mStorageRef;
    Intent intent;
    String day = "", subheading="", textdiary = "";
    ArrayList<String> photodiaty;
    EditText editText1;
    EditText editText2;
    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button)(findViewById(R.id.button1));
        editText1 = (EditText)(findViewById(R.id.editText1));
        editText2 = (EditText)(findViewById(R.id.editText2));
        textView1 = (TextView)(findViewById(R.id.textView1));

        mPostReference = FirebaseDatabase.getInstance().getReference();
//        mStorageRef = FirebaseStorage.getInstance().getReference();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, MainActivity.class);
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
    }
    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    DailyRecord get = postSnapshot.getValue(DailyRecord.class);
                    String[] info = {get.subheading, get.textdiary};
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1]);
                    editText1 = (EditText)(findViewById(R.id.editText1));
                    editText2 = (EditText)(findViewById(R.id.editText2));
                    editText1.setText(info[0]);
                    editText2.setText(info[1]);
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
            DailyRecord post = new DailyRecord(subheading, textdiary);//추후 이미지 파일 연결
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

}
