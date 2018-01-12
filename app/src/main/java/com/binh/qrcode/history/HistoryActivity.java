package com.binh.qrcode.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.binh.qrcode.R;
import com.binh.qrcode.main.ResultActivity;

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
                    break;
                case R.id.navigation_copy:
                    copyRow();
                    break;
                case R.id.navigation_share:
                    performShare();
                    break;


            }
            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

    private void performShare() {

    }

    private void copyRow() {

    }

    private void deleteRows() {
        try {
            HistoryItem historyItem = historyItems.get(listView.getSelectedItemPosition());
            historyManager.deleteItem(historyItem.getId());
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
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    openHistoryItem(historyItems.get(position));
                } catch (ArrayIndexOutOfBoundsException e) {
                    //
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    listView.setSelected(true);
                    startSupportActionMode(actionModeCallBack);
                } catch (ArrayIndexOutOfBoundsException e) {
                    //
                }
                return true;
            }
        });
    }


    private void openHistoryItem(HistoryItem historyItem) {
        Intent intent = new Intent(HistoryActivity.this, ResultActivity.class);
        intent.putExtra("text", historyItem.getResult().getText());
        intent.putExtra("format", historyItem.getResult().getBarcodeFormat().name());
        intent.putExtra("display", historyItem.getResult().getText());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
}
