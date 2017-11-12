package com.binh.qrcode;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.binh.qrcode.history.HistoryFragment;
import com.binh.qrcode.history.HistoryItem;
import com.binh.qrcode.history.HistoryManager;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

public class ScanFragment extends Fragment implements CompoundBarcodeView.TorchListener, View.OnClickListener {

    private CompoundBarcodeView barcodeView;
    private BeepManager beepManager;
    private HistoryManager manager;
    ImageView ivFlashOn, ivFlashOff;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result != null) {
                if ("false".equalsIgnoreCase(result.getText())) {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_notice_no_data_scan), Toast.LENGTH_LONG);
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
                    Bundle bundle = new Bundle();
                    bundle.putString("text", result.getText());
                    bundle.putString("format", result.getBarcodeFormat().name());
                    bundle.putString("display", result.getResult().getText());
                    bundle.putParcelable("image", result.getBitmap());
                    manager.addItem(new HistoryItem(result.getResult(), result.getResult().getText()));
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_main, ResultFragment.newInstance(bundle))
                            .addToBackStack(null)
                            .commit();
                }
            } else {
                Snackbar.make(getView().getRootView(), getString(R.string.msg_barcode_null), Snackbar.LENGTH_SHORT);
            }
            beepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public ScanFragment() {
        // Required empty public constructor
    }

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);
        manager = new HistoryManager(getContext());
        barcodeView = rootView.findViewById(R.id.zxing_barcode_scanner);
        barcodeView.setTorchListener(this);
        barcodeView.setStatusText(getString(R.string.msg_put_barcode_to_scan));
        barcodeView.decodeSingle(callback);
        beepManager = new BeepManager(getActivity());
        ivFlashOn = rootView.findViewById(R.id.btn_flash_on);
        ivFlashOff = rootView.findViewById(R.id.btn_flash_off);
        ivFlashOn.setOnClickListener(this);
        ivFlashOff.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.navigation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_history:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, HistoryFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.navigation_setting:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, SettingFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean hasFlash() {
        return getContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onTorchOn() {
    }

    @Override
    public void onTorchOff() {
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
}
