package com.example.circleprogressview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.circleprogress.CircleProgressView;

public class ProgressGradient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_gradient);
        CircleProgressView progressTrue=findViewById(R.id.progress_true);
        CircleProgressView progressFalse=findViewById(R.id.progress_false);
        progressTrue.setTextEnabled(true);
        progressFalse.setTextEnabled(true);

        progressTrue.setMaxProgress(100);
        progressFalse.setMaxProgress(100);
        progressTrue.setProgressWithAnimation(100,3000);
        progressFalse.setProgress(100);

    }
}