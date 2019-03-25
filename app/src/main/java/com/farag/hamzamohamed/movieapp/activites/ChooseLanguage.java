package com.farag.hamzamohamed.movieapp.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.farag.hamzamohamed.movieapp.R;

import java.util.Locale;

public class ChooseLanguage extends AppCompatActivity {

    String language , ret;
    Intent intent;
    SharedPreferences preferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        preferences = getSharedPreferences("myShared", Context.MODE_PRIVATE);
    }

    public void arabic(View view) {

        language = "ar";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

        intent = new Intent(ChooseLanguage.this,MainActivity.class);
        startActivity(intent);
        finish();

        preferences = getSharedPreferences("myShared", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = preferences.edit();
        myEdit.putString("lang","ar");
        myEdit.commit();

    }

    public void english(View view) {

        language = "en";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

        intent = new Intent(ChooseLanguage.this,MainActivity.class);
        startActivity(intent);
        finish();

        preferences = getSharedPreferences("myShared", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = preferences.edit();
        myEdit.putString("lang", "en");
        myEdit.commit();
    }
}
