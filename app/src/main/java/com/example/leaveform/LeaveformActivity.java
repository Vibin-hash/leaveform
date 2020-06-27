    package com.example.leaveform;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class LeaveformActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference ref;
    Button save;

    private static final String TAG = "LeaveformActivity";
    private TextInputEditText from,to;
    private String fromDate,toDate;
    private DatePickerDialog.OnDateSetListener dateSetListener1,dateSetListener2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaveform);

        save=findViewById(R.id.btn_save);
        from=findViewById(R.id.lffrom);
        to=findViewById(R.id.lfto);

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
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day+"/" + month + "/" + year;
                toDate=date;
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
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = dayOfMonth+ "/" + month + "/" + year;
                fromDate=date;
                to.setText(date);
            }
        };


        ref=FirebaseDatabase.getInstance().getReference().child("leavehistory");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("1234").child("From").setValue(fromDate);
                ref.child("1234").child("To").setValue(toDate);
            }
        });


    }
}