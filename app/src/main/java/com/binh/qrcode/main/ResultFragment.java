package com.binh.qrcode.main;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.binh.qrcode.R;
import com.binh.qrcode.history.HistoryManager;

/**
 * Created by binh on 11/11/2017.
 *
 */

public class ResultFragment extends Fragment {
    private HistoryManager historyManager;
    private ImageView imageView;
    private TextView tvResult;
    private ImageButton btnSearch, btnShare;
    private String text, format, type;

    public ResultFragment() {
    }


    public static ResultFragment newInstance(Bundle args) {
        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setArguments(args);
        return resultFragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        btnSearch = rootView.findViewById(R.id.btn_search_web);
        btnShare = rootView.findViewById(R.id.btn_share);
        imageView = rootView.findViewById(R.id.iv_barcode);
        tvResult = rootView.findViewById(R.id.tv_result);
        Bundle args = getArguments();

        try {
            imageView.setImageBitmap(args.<Bitmap>getParcelable("image"));
            text = args.getString("text") + "\n" + args.getString("display");
            format = args.getString("format");
            type = args.getString("type");
            tvResult.setText(text);
        } catch (Exception e) {

        }
        btnSearch.setVisibility(View.VISIBLE);

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
        return rootView;
    }
}
