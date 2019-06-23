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
//implemented by 양희산&이창원
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
                for (int index = 0; index < inputID.length(); index++)
                {
                    if ((inputID.charAt(index) >= '0' && inputID.charAt(index) <= '9' ) ||
                            (inputID.charAt(index) >= 'A' && inputID.charAt(index) <= 'Z') ||
                            (inputID.charAt(index) >= 'a' && inputID.charAt(index) <= 'z'))
                    {
                    }
                    else {
                        confirmedID.setText("Your ID cannot contain special characters(&,%,#)");
                        return;
                    }
                }

                if (inputID.length() > 5 && inputID.length() < 16) {
                    getFirebaseDatabase();
                }
                else
                {
                    confirmedID.setText("Your ID should be in 6~15 length");
                }
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
                intent_Signup = new Intent(Signup.this, Login.class);
                inputID = newID.getText().toString();
                inputEmail = newEmail.getText().toString();

                if (!confirmPassword.equals(inputPassword)) {
                    confirmedID.setText("Your Passwords are different");
                    return;
                }
                if (inputPassword.length() < 6 || inputPassword.length() > 15) {
                    confirmedID.setText("Your Password should be in 6~15 length");
                    return;
                }

                if (tempID.equals(inputID) == false) {
                    confirmedID.setText("You should confirm your ID");
                    return;
                }

                if ( (howmanychars(inputEmail, '@') == 1 ) &&
                        (inputEmail.indexOf("@") != 0) &&
                        (howmanychars(inputEmail, '.') == 1) &&
                        (inputEmail.indexOf("@") <  inputEmail.indexOf(".")) &&
                        (inputEmail.charAt(inputEmail.length()-1) != '.')
                )
                {
                    for (int index = 0; index < inputEmail.length(); index++)
                    {
                        if ((inputEmail.charAt(index) >= '0' && inputEmail.charAt(index) <= '9' ) ||
                                (inputEmail.charAt(index) >= 'A' && inputEmail.charAt(index) <= 'Z') ||
                                (inputEmail.charAt(index) >= 'a' && inputEmail.charAt(index) <= 'z') ||
                                (inputEmail.charAt(index) == '@' || inputEmail.charAt(index) == '.' )
                        )
                        {
                        }
                        else
                        {
                            confirmedID.setText("You should check your E-mail");
                            return;
                        }
                    }
                }
                else
                {
                    confirmedID.setText("You should check your E-mail");
                    return;
                }

                postFirebaseDatabase(true);
                startActivity(intent_Signup);
                finish();//view가 쌓이는 것을 막기 위해서 finish를 사용하여 종료시켜줌.

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
    }

    public int howmanychars(String str, char chr)
    {
        int index;
        int result = 0;
        for (index = 0; index < str.length(); index++)
        {
            if (str.charAt(index) == chr)
            {
                result++;
            }
        }
        return result;
    }
}