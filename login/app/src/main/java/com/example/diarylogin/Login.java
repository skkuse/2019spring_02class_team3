package com.example.diarylogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private DatabaseReference mPostReference;
    Intent intent_forget;
    Intent intent_login;
    Intent intent_signup;
    String inputID = "";
    String inputPassword = "";
    String inputEmail = "";
    EditText ID;
    EditText Password;
    boolean success = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        Button forget = (Button)findViewById(R.id.button_forget);
        Button login = (Button)findViewById(R.id.button_login);
        Button signup = (Button)findViewById(R.id.button_signup);
        ID = (EditText)findViewById(R.id.editText_ID);
        Password = (EditText)findViewById(R.id.editText_password);

        mPostReference = FirebaseDatabase.getInstance().getReference();



        forget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent_login = new Intent(Login.this, MainActivity.class);

                inputID = ID.getText().toString();
                inputPassword = Password.getText().toString();

                getFirebaseDatabase();

                if (success == true) {
                    //startActivity(intent_login);
                    Log.d("Login Successed", inputID);
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent_signup = new Intent(Login.this, Signup.class);
                startActivity(intent_signup);
                finish();//view가 쌓이는 것을 막기 위해서 finish를 사용하여 종료시켜줌.
            }
        });
    }

    public void getFirebaseDatabase() { ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                String key = postSnapshot.getKey();
                if (key.equals(inputID)) {
                    signupinfo get = postSnapshot.getValue(signupinfo.class);
                    String realpassword = get.Password;
                    if (realpassword.equals(inputPassword))
                    {
                        success = true;
                        return;
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
        }
    };
        Query sortbyID = mPostReference.orderByChild("id");
        sortbyID.addListenerForSingleValueEvent(postListener);
    }



}

