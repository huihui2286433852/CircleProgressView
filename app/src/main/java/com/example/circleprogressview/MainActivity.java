package com.example.circleprogressview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void outCircle(View view) {
        startActivity(new Intent(MainActivity.this, OutCircle.class));
    }

    public void progressGradient(View view) {
        startActivity(new Intent(MainActivity.this, ProgressGradient.class));
    }

    public void innerCircle(View view) {
        startActivity(new Intent(MainActivity.this, innerCircle.class));
    }
}