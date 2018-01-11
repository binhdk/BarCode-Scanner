package com.binh.qrcode.history;

import com.google.zxing.Result;

/**
 * Created by binh on 11/11/2017.
 * scanned item's data
 */

public class HistoryItem {
    private final Result result;
    private final String display;

   public HistoryItem(Result result, String display) {
        this.result = result;
        this.display = display;

    }

    public Result getResult() {
        return result;
    }

    public String getDisplayAndDetails() {
        StringBuilder displayResult = new StringBuilder();
        if (display == null || display.isEmpty()) {
            displayResult.append(result.getText());
        } else {
            displayResult.append(display);
        }
        return displayResult.toString();
    }

}
