package com.binh.qrcode.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.binh.qrcode.R;

/**
 * Created by binh on 11/11/2017.
 * user preference setting
 */

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.title_setting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
