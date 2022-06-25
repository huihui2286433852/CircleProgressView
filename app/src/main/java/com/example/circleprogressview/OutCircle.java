package com.example.circleprogressview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.circleprogress.CircleProgressView;

public class OutCircle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_circle);
        CircleProgressView progressTrue=findViewById(R.id.progress_true);
        CircleProgressView progressFalse=findViewById(R.id.progress_false);
        progressTrue.setTextEnabled(true);
        progressFalse.setTextEnabled(true);

        progressTrue.setMaxProgress(100);
        progressFalse.setMaxProgress(100);
        progressTrue.setProgressWithAnimation(70,3000);
        progressFalse.setProgressWithAnimation(70,3000);
    }
}