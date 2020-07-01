package com.example.leaveform;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class LeaveformActivity extends AppCompatActivity {


    FirebaseAuth leaveAuth;
    FirebaseDatabase database;
    DatabaseReference ref,sRef,sRef2,countRef;
    Button save;
    String strserialNo="1",leaveCount,d,reasonForLeave;
    long sno,temp,diff;


    Counttheleave.Date dfrom,dto;

    private static final String TAG = "LeaveformActivity";
    private TextInputEditText from,to;
    private TextInputLayout reason;
    private String fromDate,toDate;
    private DatePickerDialog.OnDateSetListener dateSetListener1,dateSetListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaveform);

        save=findViewById(R.id.btn_save);
        from=findViewById(R.id.lffrom);
        to=findViewById(R.id.lfto);
        reason=findViewById(R.id.lf_reason);


        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        LeaveformActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener1,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day+"-" + month + "-" + year;

                dfrom=new Counttheleave.Date(day,month,year);

                fromDate=date;
                from.setText(date);
            }
        };

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year1=calendar.get(Calendar.YEAR);
                int month1=calendar.get(Calendar.MONTH);
                int day1=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog1=new DatePickerDialog(LeaveformActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener2,year1,month1,day1);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialog1.show();
            }
        });

        dateSetListener2=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day+"-"+ month + "-" + year;
                dto=new Counttheleave.Date(day,month,year);

                toDate=date;
                to.setText(date);

            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        final String uid=user.getUid();


        sRef=FirebaseDatabase.getInstance().getReference().child("serialNumber").child(uid);
        countRef=FirebaseDatabase.getInstance().getReference().child("leavehistory").child(uid).child("Totalleave");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        reasonForLeave = reason.getEditText().getText().toString();
                        strserialNo = dataSnapshot.getValue().toString();
                        d = String.valueOf(Counttheleave.getDifference(dfrom, dto));
                        diff = Integer.parseInt(d) + 1;
                        if (diff <= 0) {
                            Toast.makeText(LeaveformActivity.this,"Choose the appropriate date",Toast.LENGTH_SHORT).show();
                        }
                        else if(reasonForLeave.isEmpty()) {
                            reason.setError("Enter a reason");
                        }
                        else
                         {
                             reason.setError(null);

                            ref = FirebaseDatabase.getInstance().getReference().child("leavehistory");


                            ref.child(uid).child(strserialNo).child("From").setValue(fromDate);
                            ref.child(uid).child(strserialNo).child("To").setValue(toDate);
                            ref.child(uid).child(strserialNo).child("Reason").setValue(reasonForLeave);

                            sno = Integer.parseInt(strserialNo);
                            if (sno == 0) {
                                ref.child(uid).child("Totalleave").setValue(0);
                            } else {
                                countRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        leaveCount = dataSnapshot.getValue().toString();
                                        temp = Integer.parseInt(leaveCount);
                                        temp = temp + diff;
                                        countRef.setValue(temp);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            sno = sno + 1;
                            sRef2 = FirebaseDatabase.getInstance().getReference().child("serialNumber");
                            sRef2.child(uid).setValue(sno);
                            Toast.makeText(LeaveformActivity.this, d, Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}