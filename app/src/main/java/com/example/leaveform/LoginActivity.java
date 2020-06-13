package com.example.leaveform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView hello;
    Button go,newuser;
    TextInputLayout loginusername,loginpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginusername=findViewById(R.id.username);
        loginpassword=findViewById(R.id.password);
        hello=findViewById(R.id.hello);

        go=(Button)findViewById(R.id.login_loginbtn);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View v) {
                loginuser(v);
            }
        });

        newuser=(Button)findViewById(R.id.login_newuserbtn);
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(j);
            }
        });
    }

    private Boolean validateUserName()
    {
        String val=loginusername.getEditText().getText().toString();

        if(val.isEmpty())
        {
            loginusername.setError("Cannot be Empty");
            return false;
        }
        else {
            loginusername.setError(null);
            loginusername.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePassword()
    {
        String val=loginpassword.getEditText().getText().toString();

        if(val.isEmpty())
        {
            loginpassword.setError("Cannot be empty");
            return false;
        }
        else {
            loginpassword.setError(null);
            return true;
        }
    }

    public void loginuser(View view)
    {
        if(!validateUserName() | !validatePassword())
        {
            return;
        }
        else
        {
            isUser();
        }
    }

    private void isUser()
    {
        final String userEnteredUsername=loginusername.getEditText().getText().toString().trim();
        final String userEnteredPassword=loginpassword.getEditText().getText().toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");

        Query checkUser=reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        loginusername.setError(null);
                        loginusername.setErrorEnabled(false);
                        String passwordfromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);
                        hello.setText(passwordfromDB);


                        if (passwordfromDB!=null && passwordfromDB.equals(userEnteredPassword)) {
                            String namefromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                            String usernamefromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                            String emailfromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                            String phnofromDB = dataSnapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);

                            Intent intent = new Intent(LoginActivity.this, LeaveformActivity.class);

                            intent.putExtra("name",namefromDB);
                            intent.putExtra("username",usernamefromDB);
                            intent.putExtra("email",emailfromDB);
                            intent.putExtra("phoneNo",phnofromDB);
                            intent.putExtra("password",passwordfromDB);

                            startActivity(intent);

                        }
                        else
                        {
                            loginpassword.setError("Wrong password");
                            loginpassword.requestFocus();
                        }
                    }
                    else
                    {
                        loginusername.setError("No such user exists");
                        loginusername.requestFocus();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}