package com.farag.hamzamohamed.movieapp.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.farag.hamzamohamed.movieapp.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashImage = findViewById(R.id.splash);

        int[] ids = new int[]{R.drawable.splashhh
                ,R.drawable.splash1
                ,R.drawable.splash3
                ,R.drawable.splash4
                ,R.drawable.splash6
                ,R.drawable.splash9 };

        Random random = new Random();
        int r = random.nextInt(ids.length);
        this.splashImage.setImageDrawable(getResources().getDrawable(ids[r]));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this,ChooseLanguage.class));
                finish();
            }
        },2500);
    }
}
