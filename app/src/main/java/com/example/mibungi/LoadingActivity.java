package com.example.mibungi;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;

public class LoadingActivity extends AppCompatActivity {

    @BindView(R.id.progress)
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);



    }
}
