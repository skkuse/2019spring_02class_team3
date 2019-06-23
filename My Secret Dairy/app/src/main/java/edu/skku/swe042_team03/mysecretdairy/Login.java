package edu.skku.swe042_team03.mysecretdairy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
//implemented by 양희산&이창원
public class Login extends AppCompatActivity {

    private DatabaseReference mPostReference;
    Intent intent_login;
    Intent intent_signup;
    String inputID = "";
    String inputPassword = "";
    String tempID = "";
    String tempPassword = "";
    EditText ID;
    EditText Password;
    boolean success = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button login = (Button)findViewById(R.id.button1);
        Button signup = (Button)findViewById(R.id.button2);
        ID = (EditText)findViewById(R.id.editText1);
        Password = (EditText)findViewById(R.id.editText2);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new View.OnClickListener() {//login 버튼을 누를시에, 파이어베이스와 연동하여, 그값을 비교함
            public void onClick(View view) {

                inputID = ID.getText().toString();
                inputPassword = Password.getText().toString();

                getFirebaseDatabase();

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {//sign up 버튼을 누를시에, Signup activity로 액티비티를 변환시켜줌
            public void onClick(View view) {
                intent_signup = new Intent(Login.this, Signup.class);
                startActivity(intent_signup);
            }
        });
    }

    public void getFirebaseDatabase() {//id_list 아래의 모든 아이디와 아이디, 비밀번호를 비교함. return()을 사용하여, 함수의 종료를, finish()를 사용하여 actvity의 종료를 수행하였다.
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    SignupInfo get = postSnapshot.getValue(SignupInfo.class);
                    String[] info = {get.ID, get.Password, get.Email};
                    tempID = get.ID;
                    tempPassword = get.Password;
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2]);
                    if((tempID.equals((inputID))) && (tempPassword.equals((inputPassword)))){
                            intent_login = new Intent(Login.this, CalendarView.class);
                            intent_login.putExtra("id",inputID);
                            startActivity(intent_login);
                            finish();
                            return;
                    }
                }
                Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("id_list").addValueEventListener(postListener);
        //Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
    }
}

