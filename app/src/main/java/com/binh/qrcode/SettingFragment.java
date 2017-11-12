package com.binh.qrcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by binh on 11/11/2017.
 */

public class SettingFragment extends Fragment {

    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_setting, container, false);
        return rooView;
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }
}
