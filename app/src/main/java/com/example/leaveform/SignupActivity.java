package com.example.leaveform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    TextView backtologin;
    TextInputLayout regfullname,regusername,regemail,regpassword,regphno;
    Button register;
    String name,password,email,username,phoneno;
    FirebaseAuth mAuth;
    FirebaseDatabase rootnode;
    DatabaseReference reference,serialref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth=FirebaseAuth.getInstance();
        backtologin=(TextView)findViewById(R.id.signup_gotologinpage);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(k);
                finish();
            }
        });

        regfullname=findViewById(R.id.signup_name);
        regusername=findViewById(R.id.signup_username);
        regemail=findViewById(R.id.signup_Email);
        regpassword=findViewById(R.id.signup_password);
        regphno=findViewById(R.id.signup_phno);
        register=findViewById(R.id.reg_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootnode=FirebaseDatabase.getInstance();


                reference = rootnode.getReference("users");

                if(!validateName() | !validateEmail() | !validatePassword() | !validatePhno() | !validateUserName() )
                {
                    return;
                }

                name =regfullname.getEditText().getText().toString();
                username=regusername.getEditText().getText().toString();
                email=regemail.getEditText().getText().toString();
                password=regpassword.getEditText().getText().toString();
                phoneno=regphno.getEditText().getText().toString();



                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    assert user != null;

                                    String userUid=user.getUid();
                                    UserHelperClass helperClass=new UserHelperClass(name,username,email,phoneno,password);
                                    reference.child(userUid).setValue(helperClass);

                                    serialref=FirebaseDatabase.getInstance().getReference().child("serialNumber");
                                    serialref.child(userUid).setValue(0);

                                    startActivity(new Intent(SignupActivity.this,LeaveformActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });


            }
        });

    }


    private Boolean validateName()
    {
        String val=regfullname.getEditText().getText().toString();

        if(val.isEmpty())
        {
            regfullname.setError("Cannot be empty");
            return false;
        }
        else {
            regfullname.setError(null);
            regfullname.setErrorEnabled(false);  //remove previously entered space
            return true;
        }
    }

    private Boolean validateUserName()
    {
        String val=regusername.getEditText().getText().toString();

        if(val.isEmpty())
        {
            regusername.setError("Cannot be Empty");
            return false;
        }
        else if(val.length()>=20){
            regusername.setError("Username too long");
            return false;
                }
        else {
            regusername.setError(null);
            regusername.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateEmail()
    {
        String val=regemail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty())
        {
            regemail.setError("Cannot be empty");
            return false;
        }
        else if (!val.matches(emailPattern)) {
            regemail.setError("Invalid Email");
            return false;
        }
        else {
            regemail.setError(null);
            return true;
        }
    }

    private Boolean validatePassword()
    {
        String val=regpassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if(val.isEmpty())
        {
            regpassword.setError("Cannot be empty");
            return false;
        }
        else if (!val.matches(passwordVal)) {
            regpassword.setError("Password is too weak");
            return false;
        }
        else {
            regpassword.setError(null);
            return true;
        }
    }

    private Boolean validatePhno()
    {
        String val=regphno.getEditText().getText().toString();

        if(val.isEmpty())
        {
            regphno.setError("Cannot be empty");
            return false;
        }
        else {
            regphno.setError(null);
            return true;
        }
    }

}