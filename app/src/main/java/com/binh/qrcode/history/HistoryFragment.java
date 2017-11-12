package com.binh.qrcode.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.binh.qrcode.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 11/11/2017.
 */

public class HistoryFragment extends Fragment {
    private List<HistoryItem> historyItemList;
    private HistoryItemAdapter adapter;
    private HistoryManager historyManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ListView listView = rootView.findViewById(R.id.list_view_history);
        historyItemList = new ArrayList<>();
        historyManager = new HistoryManager(getContext());
        historyItemList.addAll(historyManager.getAll());
        adapter = new HistoryItemAdapter(getContext(), R.id.list_view_history);
        adapter.addAll(historyItemList);
        listView.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_detail, menu);
        menu.getItem(0).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.msg_sure);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i2) {
                        historyManager.clearHistory();
                        dialog.dismiss();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                builder.setNegativeButton(R.string.button_cancel, null);
                builder.show();
                break;
            default:
            case R.id.navigation_share:
                share();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {

    }
}
