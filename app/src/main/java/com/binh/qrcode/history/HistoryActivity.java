package com.binh.qrcode.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.binh.qrcode.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 11/11/2017.
 * history page
 */

public class HistoryActivity extends AppCompatActivity {
    private List<HistoryItem> historyItems;
    private HistoryItemAdapter adapter;
    private HistoryManager historyManager;
    private ListView listView;
    private ActionMode actionMode;
    private Menu menu;
    private ActionMode.Callback actionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_detail, menu);//Inflate the menu over action mode
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.findItem(R.id.navigation_search).setVisible(false);
            menu.findItem(R.id.navigation_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.navigation_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.navigation_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_delete:
                    deleteRows();
                    hideActionMode();//finish actionmode after do something
                    return true;
                case R.id.navigation_copy:
                    copyRow();
                    hideActionMode();//finish actionmode after do something
                    return true;
                case R.id.navigation_share:
                    performShare();
                    hideActionMode();//finish actionmode after do something
                    return true;
            }
            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode=null;
        }
    };

    private void performShare() {

    }

    private void copyRow() {

    }

    private void deleteRows() {
        SparseBooleanArray selected = adapter.getSelectedIds();
        try {
            for (int i = 0; i < selected.size(); i++) {
                if (selected.get(i)) {
                    HistoryItem historyItem = historyItems.remove(i);
                    historyManager.deleteItem(historyItem.getId());
                    adapter.remove(historyItem);
                    adapter.notifyDataSetChanged();
                }
            }
            listView.invalidateViews();

        } catch (Exception e) {
            Log.e(HistoryActivity.class.getSimpleName(), "Error ", e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle(R.string.title_history);
        final Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list_view_history);
        historyItems = new ArrayList<>();
        historyManager = new HistoryManager(getApplicationContext());
        historyItems.addAll(historyManager.getAll());
        adapter = new HistoryItemAdapter(this, R.id.list_view_history);
        adapter.addAll(historyItems);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode != null)
                    onListItemSelected(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemSelected(position);
                return true;
            }
        });
    }

    private void onListItemSelected(int position) {
        try {
            adapter.toggleSelection(position);
            boolean hasCheckedItems = adapter.getSelectedCount() > 0;
            if (hasCheckedItems && actionMode == null)
                actionMode = startSupportActionMode(actionModeCallBack);
            else if (!hasCheckedItems && actionMode != null)
                actionMode.finish();
            if (actionMode != null)
                actionMode.setTitle(String.valueOf(adapter
                        .getSelectedCount()));
        } catch (Exception e) {
            Log.e(HistoryActivity.class.getSimpleName(), "error", e);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_delete:
                enableMultiChoiceListView();
                if (historyItems.isEmpty())
                    return true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.msg_sure);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i2) {
                        historyManager.clearHistory();
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        listView.invalidate();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.button_cancel, null);
                builder.show();
                return true;
            case R.id.navigation_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {

    }

    private void enableMultiChoiceListView() {

    }

    public void hideActionMode() {
        if (actionMode != null)
            actionMode.finish();
    }
}
