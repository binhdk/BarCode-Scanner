package com.binh.qrcode.main;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageButton btnSearch = findViewById(R.id.btn_search_web);
        ImageButton btnShare = findViewById(R.id.btn_share);
        imageView = findViewById(R.id.iv_barcode);
        TextView tvResult = findViewById(R.id.tv_result);
        btnSearch.setVisibility(View.VISIBLE);
        try {
            imageView.setImageBitmap((Bitmap) getIntent().getParcelableExtra("image"));
            text = getIntent().getStringExtra("text");
            String format = getIntent().getStringExtra("format");
            tvResult.setText(text);
        } catch (Exception e) {
            //
        }
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, text);
                startActivity(intent);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
