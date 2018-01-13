package com.binh.qrcode.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.binh.qrcode.R;

/**
 * Created by binh on 1/13/2018.
 * custom relative-layout can check
 */

public class CheckableRelativeLayout extends RelativeLayout
        implements Checkable {

    private boolean checked = false;

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        View layout = findViewById(R.id.rl_item_history);
        CheckBox checkBox = layout.findViewById(R.id.checkbox_history);
        if (checked) {
            checkBox.setChecked(true);
            checkBox.setVisibility(VISIBLE);
        } else {
            checkBox.setChecked(false);
        }

    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        this.checked = !this.checked;
        setChecked(this.checked);
    }
}
