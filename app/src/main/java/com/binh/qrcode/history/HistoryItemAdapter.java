package com.binh.qrcode.history;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.binh.qrcode.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 11/11/2017.
 * adapter for history scanned item
 */

public class HistoryItemAdapter extends ArrayAdapter<HistoryItem> {
    private Context activity;
    private SparseBooleanArray selectedItems;
    public HistoryItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        activity = context;
        selectedItems = new SparseBooleanArray();
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
            viewHolder.checkbox = convertView.findViewById(R.id.checkbox_history);
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
        applyIconAnimation(viewHolder, position);
        return convertView;
    }

    private void applyIconAnimation(ViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setVisibility(View.GONE);
        }
    }


    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyDataSetChanged();
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }
    public int getSelectedCount() {
        return selectedItems.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedItems;
    }

    static class ViewHolder {
        CheckBox checkbox;
        TextView title, detail;
    }

}

