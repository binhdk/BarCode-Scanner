package com.binh.qrcode.history;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.binh.qrcode.R;

/**
 * Created by binh on 11/11/2017.
 * adapter for history scanned item
 */

public class HistoryItemAdapter extends ArrayAdapter<HistoryItem> {
    private Context activity;
    private int selectedPos;

    public HistoryItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        activity = context;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater factory = LayoutInflater.from(activity);
            convertView = factory.inflate(R.layout.layout_item_history, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.history_title);
            viewHolder.detail = convertView.findViewById(R.id.history_detail);
            convertView.setTag(viewHolder);
        }
        try {
            HistoryItem item = getItem(position);
            if (item != null) {
                viewHolder.title.setText(item.getResult().getText());
                viewHolder.detail.setText(item.getDisplayAndDetails());
            } else {
                Resources resources = getContext().getResources();
                viewHolder.title.setText(resources.getString(R.string.history_empty));
                viewHolder.detail.setText(resources.getString(R.string.history_empty_detail));
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(HistoryItemAdapter.class.getSimpleName(), "error out of index", e);
        } catch (Exception e1) {
            Log.e(HistoryItemAdapter.class.getSimpleName(), "error", e1);
        }
        return convertView;
    }


    public void clearSelections() {
        selectedPos = -1;
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    static class ViewHolder {
        TextView title, detail;
    }

}

