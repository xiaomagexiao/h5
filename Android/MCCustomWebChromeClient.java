package com.whatyplugin.imooc.logic.JSBridge;

import android.app.Activity;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.whatyplugin.base.log.MCLog;
import com.whatyplugin.imooc.logic.contants.Contants;
import com.whatyplugin.imooc.logic.save.MCSaveData;
import com.whatyplugin.imooc.ui.mymooc.MoocApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class MCCustomWebChromeClient extends WebChromeClient {

    private Activity mContext = null;
    private MCClientProxy mClientProxy = null;

    private static final String TAG = "MCCustomWebChromeClient";

    public MCCustomWebChromeClient(Activity context) {
        mContext = context;
        mClientProxy = new MCClientProxy(mContext);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        result.confirm();
        if (mContext != null) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        mClientProxy.openConfirmDialog(message, result);
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        MCLog.e(TAG, "onJsPrompt: " + message);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            return true;
        }

        String cmd = jsonObject.optString("command");
        if ("openview".equals(cmd)) {
            mClientProxy.openView(jsonObject.optString("link"),jsonObject.optString("title"));
            result.confirm("{\"result\":\"1\"}");
        } else if ("select".equals(cmd)) {
            mClientProxy.openSelectDialog("111", jsonObject.optString("options"), result);
        }else if ("userInfo".equals(cmd)){
            String loginType = (String) MCSaveData.getUserInfo(Contants.LOGINTYPE, MoocApplication.getInstance());
            result.confirm("{\"loginType\":\""+loginType+"\"}");
        }else if ("opencourse".equals(cmd)){
            mClientProxy.openCourse(jsonObject.optString("title"),jsonObject.optString("courseId"),jsonObject.optString("imageUrl"));
            result.confirm("{\"result\":\"1\"}");
        }
        return true;
    }

    @Override
    public void onProgressChanged(final WebView view, int newProgress) {
        if(newProgress == 100){
                    view.loadUrl("javascript:android_app_ready()");
                    MCClientProxy.refreshLoginType(view);
        }
        super.onProgressChanged(view, newProgress);
    }

}
