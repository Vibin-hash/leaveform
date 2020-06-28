package com.example.leaveform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView hello;
    Button go,newuser;
    TextInputLayout loginemail,loginpassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginemail=findViewById(R.id.username);
        loginpassword=findViewById(R.id.password);
        hello=findViewById(R.id.hello);
        mAuth=FirebaseAuth.getInstance();

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

    private Boolean validateEmail()
    {
        String val=loginemail.getEditText().getText().toString();

        if(val.isEmpty())
        {
            loginemail.setError("Cannot be Empty");
            return false;
        }
        else {
            loginemail.setError(null);
            loginemail.setErrorEnabled(false);
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
        if(!validateEmail() | !validatePassword())
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
        String password=loginpassword.getEditText().getText().toString();
        String email=loginemail.getEditText().getText().toString();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(LoginActivity.this,LeaveformActivity.class));
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}