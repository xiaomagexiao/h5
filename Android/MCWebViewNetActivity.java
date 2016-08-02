package com.whatyplugin.imooc.logic.JSBridge;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.whatyplugin.imooc.logic.utils.WebViewUtil;
import com.whatyplugin.imooc.ui.view.BaseTitleView;

import cn.com.whatyplugin.mooc.R;

public class MCWebViewNetActivity extends Activity {

    private WebView wv_content;
    private BaseTitleView titleView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.webview_activity);
        this.wv_content = (WebView) this.findViewById(R.id.wv_content);
        this.titleView = (BaseTitleView) this.findViewById(R.id.rl_titile);
        WebViewUtil.configWebview(wv_content);
        wv_content.setWebChromeClient(new MCCustomWebChromeClient(this));
        wv_content.setWebViewClient(new MCCustomWebViewClient(this));
        String url = this.getIntent().getExtras() == null ? "" : this.getIntent().getExtras().getString("link");
        String title = this.getIntent().getExtras() == null ? "whaty" : this.getIntent().getExtras().getString("title");
        titleView.setTitle(title);
        wv_content.loadUrl(url);

    }

}
