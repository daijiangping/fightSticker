package com.acey.fightsticker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    public void start(View view) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }
}
