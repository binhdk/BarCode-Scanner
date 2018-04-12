package com.binh.qrcode.main;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binh.qrcode.R;

/**
 * Created by binh on 11/11/2017.
 * display result
 */

public class ResultActivity extends AppCompatActivity {
    private ImageView imageView;
    private String text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle(R.string.title_result);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView ivSearch = findViewById(R.id.iv_search_web);
        ImageView ivShare = findViewById(R.id.iv_share);
        ImageView ivCopy = findViewById(R.id.iv_copy);
        imageView = findViewById(R.id.iv_barcode);
        final TextView tvResult = findViewById(R.id.tv_result);
        try {
            imageView.setImageBitmap((Bitmap) getIntent().getParcelableExtra("image"));
            text = getIntent().getStringExtra("text");
            String format = getIntent().getStringExtra("format");
            tvResult.setText(text);
        } catch (Exception e) {
            //
        }
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, text);
                startActivity(intent);
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                String shareBody = text;
                String shareSubject = "Share data from QrCode scanner";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageView.getDrawingCache());
                startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_intent)));
            }
        });

        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("Content is copied", tvResult.getText());
                if (manager != null) {
                    manager.setPrimaryClip(data);
                    Toast.makeText(ResultActivity.this, "Text is copied", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
