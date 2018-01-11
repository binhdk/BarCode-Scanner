package com.binh.qrcode.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle(R.string.title_history);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.list_view_history);
        historyItems = new ArrayList<>();
        historyManager = new HistoryManager(getApplicationContext());
        historyItems.addAll(historyManager.getAll());
        adapter = new HistoryItemAdapter(this, R.id.list_view_history);
        adapter.addAll(historyItems);
        listView.setAdapter(adapter);

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
                    onHistoryItemLongClick(historyItems.get(position));
                } catch (ArrayIndexOutOfBoundsException e) {
                    //
                }
                return true;
            }
        });
    }

    private void onHistoryItemLongClick(HistoryItem historyItem) {
        Intent intent = new Intent(HistoryActivity.this, ResultActivity.class);
        intent.putExtra("text", historyItem.getResult().getText());
        intent.putExtra("format", historyItem.getResult().getBarcodeFormat().name());
        intent.putExtra("display", historyItem.getResult().getText());
        startActivity(intent);
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
        menu.getItem(0).setVisible(true);
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
                        historyItems.clear();
                        adapter.notifyDataSetChanged();
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
