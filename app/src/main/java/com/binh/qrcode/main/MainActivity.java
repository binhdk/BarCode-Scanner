package com.binh.qrcode.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.binh.qrcode.R;
import com.binh.qrcode.history.HistoryActivity;
import com.binh.qrcode.history.HistoryItem;
import com.binh.qrcode.history.HistoryManager;
import com.binh.qrcode.setting.SettingActivity;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements
        DecoratedBarcodeView.TorchListener,
        View.OnClickListener {
    private final int REQUEST_CODE = 1;
    private CompoundBarcodeView barcodeView;
    private BeepManager beepManager;
    private HistoryManager manager;
    ImageView ivFlashOn, ivFlashOff;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result != null) {
                if ("false".equalsIgnoreCase(result.getText())) {
                    Toast toast = Toast.makeText(
                            MainActivity.this, getString(R.string.msg_notice_no_data_scan), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                    barcodeView.pause();
                    barcodeView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                barcodeView.resume();
                                barcodeView.decodeSingle(callback);
                                return true;
                            } else
                                return false;
                        }
                    });
                } else {
                    manager.addItem(new HistoryItem(result.getResult(), result.getResult().getText()));
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("text", result.getText());
                    intent.putExtra("format", result.getBarcodeFormat().name());
                    intent.putExtra("image", result.getBitmap());
                    startActivity(intent);
                }
            } else {
                Toast toast = Toast.makeText(
                        MainActivity.this, getString(R.string.msg_barcode_null), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            beepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

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
        manager = new HistoryManager(this);
        barcodeView = findViewById(R.id.zxing_barcode_scanner);
        barcodeView.setTorchListener(this);
        barcodeView.setStatusText(getString(R.string.msg_put_barcode_to_scan));
        barcodeView.decodeSingle(callback);
        beepManager = new BeepManager(this);
        ivFlashOn = findViewById(R.id.btn_flash_on);
        ivFlashOff = findViewById(R.id.btn_flash_off);
        ivFlashOn.setOnClickListener(this);
        ivFlashOff.setOnClickListener(this);
    }
    @Override
    public void onResume() {
        barcodeView.resume();
        barcodeView.decodeSingle(callback);
        super.onResume();

    }

    @Override
    public void onPause() {
        barcodeView.pause();
        super.onPause();
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

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }

    private boolean hasFlash() {
        return getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_flash_on:
                if (hasFlash())
                    barcodeView.setTorchOn();
                ivFlashOff.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                break;
            case R.id.btn_flash_off:
                view.setVisibility(View.GONE);
                ivFlashOn.setVisibility(View.VISIBLE);
                if (hasFlash())
                    barcodeView.setTorchOff();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
