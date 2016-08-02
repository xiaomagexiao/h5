package com.whatyplugin.imooc.logic.JSBridge;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MCCustomWebViewClient extends WebViewClient {

    private Context aa = null;

    public MCCustomWebViewClient(Context aas) {
        aa = aas;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

}
