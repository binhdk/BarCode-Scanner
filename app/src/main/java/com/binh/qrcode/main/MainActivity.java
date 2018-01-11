package com.binh.qrcode.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.binh.qrcode.R;
import com.binh.qrcode.history.HistoryActivity;
import com.binh.qrcode.setting.SettingActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        requestPermissions();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(
                R.id.content_main, ScanFragment.newInstance(), ScanFragment.class.getSimpleName());
        fm.popBackStack(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                return true;
            case R.id.navigation_history:
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION, CAMERA},
                        REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean accessLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean callPhone = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (!accessLocation || !callPhone) {
                        Snackbar.make(findViewById(android.R.id.content),
                                getString(R.string.msg_require_camera), Snackbar.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(this,
                                new String[]{ACCESS_FINE_LOCATION, CAMERA},
                                REQUEST_CODE);
                    }
                }
                break;
        }
    }

}
