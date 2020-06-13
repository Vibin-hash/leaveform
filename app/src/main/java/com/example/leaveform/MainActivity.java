package com.example.leaveform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leaveform.R;

public class MainActivity extends AppCompatActivity {

    Animation top,bot;
    ImageView fire;
    TextView eshwar;
    private static int SPLASH_SCREEN=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=21)
        {
            Window window=this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.Black));
        }

//        //Animations
//        top= AnimationUtils.loadAnimation(this,R.anim.top_animation);
//        bot=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
//
//        fire=findViewById(R.id.fireimage);
//        eshwar=findViewById(R.id.tveshwar);
//        eshwar.setAnimation(bot);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }

        },SPLASH_SCREEN);


    }
}
