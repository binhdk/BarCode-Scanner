package com.binh.qrcode.history;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binh.qrcode.R;
import com.google.zxing.Result;

/**
 * Created by binh on 11/11/2017.
 * adapter for history scanned item
 */

public class HistoryItemAdapter extends ArrayAdapter<HistoryItem> {
    private Context activity;

    public HistoryItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup viewGroup) {
        View layout;
        if (view instanceof LinearLayout) {
            layout = view;
        } else {
            LayoutInflater factory = LayoutInflater.from(activity);
            layout = factory.inflate(R.layout.layout_item_history, viewGroup, false);
        }

        HistoryItem item = getItem(position);
        Result result = null;
        if (item != null) {
            result = item.getResult();
        }
        CharSequence title;
        CharSequence detail;
        if (result != null) {
            title = result.getText();
            detail = item.getDisplayAndDetails();
        } else {
            Resources resources = getContext().getResources();
            title = resources.getString(R.string.history_empty);
            detail = resources.getString(R.string.history_empty_detail);
        }

        ((TextView) layout.findViewById(R.id.history_title)).setText(title);
        ((TextView) layout.findViewById(R.id.history_detail)).setText(detail);

        return layout;
    }

}

