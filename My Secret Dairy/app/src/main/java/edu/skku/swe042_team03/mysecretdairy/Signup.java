package edu.skku.swe042_team03.mysecretdairy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private DatabaseReference mPostReference;
    Intent intent_cancel;
    Intent intent_Signup;
    String inputID = "";
    String inputPassword = "";
    String confirmPassword = "";
    String inputEmail = "";
    EditText newID;
    EditText newPassword;
    EditText confirmedPassword;
    EditText newEmail;
    TextView confirmedID;
    static String tempID = "";
    boolean add = false;
    static boolean correctid = false;
    static boolean correctpassword = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button confirmID = (Button)findViewById(R.id.button_ConfirmID);
        Button cancel = (Button)findViewById(R.id.button_Cancel);
        Button signup = (Button)findViewById(R.id.button_Signup);
        newID = (EditText)findViewById(R.id.editText_NewID);
        newPassword = (EditText)findViewById(R.id.editText_NewPassword);
        confirmedPassword = (EditText)findViewById(R.id.editText_Confirm);
        newEmail = (EditText)findViewById(R.id.editText_Email);
        confirmedID = (TextView)findViewById(R.id.textView_ConfirmID);
        mPostReference = FirebaseDatabase.getInstance().getReference();

        confirmID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                inputID = newID.getText().toString();
                getFirebaseDatabase();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent_cancel = new Intent(Signup.this, Login.class);
                startActivity(intent_cancel);
                finish();//view가 쌓이는 것을 막기 위해서 finish를 사용하여 종료시켜줌.
                }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                confirmPassword = confirmedPassword.getText().toString();
                inputPassword = newPassword.getText().toString();

                if (confirmPassword.equals(inputPassword)) {
                    correctpassword = true;
                }
                else {
                    correctpassword = false;
                }

                intent_Signup = new Intent(Signup.this, Login.class);
                inputID = newID.getText().toString();
                inputPassword = newPassword.getText().toString();
                inputEmail = newEmail.getText().toString();

                if (tempID.equals(inputID) == false) {
                    correctid = false;
                }

                if (correctpassword == true && correctid == true) {
                    postFirebaseDatabase(true);
                    startActivity(intent_Signup);
                    finish();//view가 쌓이는 것을 막기 위해서 finish를 사용하여 종료시켜줌.
                }
                else if (correctpassword == true && correctid == false) {
                    confirmedID.setText("You should confirm your ID");
                }
                else if (correctid == true && correctpassword == false) {
                    confirmedID.setText("Your passwords are different");
                }
                else {
                    confirmedID.setText("You should confirm your ID");
                }
            }
        });

    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    if (key.equals(inputID)) {
                        correctid = false;
                        confirmedID.setText("The ID is already exists");
                        return;
                    }
                }
                correctid = true;
                confirmedID.setText("You can use this ID");
                tempID = newID.getText().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("id_list").addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            SignupInfo post = new SignupInfo(inputID, inputPassword, inputEmail);
            postValues = post.toMap();
        }
        childUpdates.put("/id_list/"+inputID, postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void clearET () {
        inputID = "";
        inputPassword = "";
        inputEmail = "";
    }

}
