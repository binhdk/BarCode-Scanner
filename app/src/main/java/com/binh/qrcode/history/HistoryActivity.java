package com.binh.qrcode.history;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.binh.qrcode.R;
import com.binh.qrcode.main.MainActivity;
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
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_detail, menu);//Inflate the menu over action mode
            menu.findItem(R.id.navigation_delete).setVisible(true);
            menu.findItem(R.id.navigation_copy).setVisible(true);
            menu.findItem(R.id.navigation_share).setVisible(true);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.findItem(R.id.navigation_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.navigation_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.navigation_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_delete:
                    deleteRow();
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
            actionMode = null;
        }
    };

    private void performShare() {
        try {
            int pos = adapter.getSelectedPos();
            if (pos >= 0) {
                HistoryItem selectedItem = historyItems.get(pos);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                String shareBody = selectedItem.getDisplayAndDetails();
                String shareSubject = "Share data from QrCode scanner";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_STREAM, selectedItem.getResult().getRawBytes());
                startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_intent)));
            } else {
                Toast.makeText(HistoryActivity.this, "Nothing to share", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(HistoryActivity.this, "Nothing is copied", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyRow() {
        try {
            int pos = adapter.getSelectedPos();
            if (pos >= 0) {
                HistoryItem selectedItem = historyItems.get(adapter.getSelectedPos());
                ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("Content is copied", selectedItem.getDisplayAndDetails());
                if (manager != null) {
                    manager.setPrimaryClip(data);
                    Toast.makeText(HistoryActivity.this, "Text is copied", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(HistoryActivity.this, "Nothing is copied", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(HistoryActivity.this, "Nothing is copied", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRow() {
        try {
            int pos = adapter.getSelectedPos();
            if (pos >= 0) {
                HistoryItem historyItem = historyItems.remove(pos);
                historyManager.deleteItem(historyItem.getId());
                adapter.remove(historyItem);
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
            }

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
        adapter.setSelectedPos(position);
        try {
            if (actionMode == null)
                actionMode = startSupportActionMode(actionModeCallBack);
            else
                actionMode.finish();
        } catch (Exception e) {
            Log.e(HistoryActivity.class.getSimpleName(), "error", e);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (adapter.isEmpty()) {
            menu.findItem(R.id.navigation_delete).setVisible(false);
        } else {
            menu.findItem(R.id.navigation_delete).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void hideActionMode() {
        if (actionMode != null)
            actionMode.finish();
    }
}
