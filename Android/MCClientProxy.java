package com.whatyplugin.imooc.logic.JSBridge;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.widget.AdapterView;

import com.whatyplugin.base.dialog.MCCreateDialog;
import com.whatyplugin.base.log.MCLog;
import com.whatyplugin.imooc.logic.contants.Contants;
import com.whatyplugin.imooc.logic.model.MCCourseModel;
import com.whatyplugin.imooc.logic.save.MCSaveData;
import com.whatyplugin.imooc.ui.mymooc.MoocApplication;
import com.whatyplugin.imooc.ui.showmooc.ShowMoocActivity;
import com.whatyplugin.uikit.dialog.MCCommonDialog;

import java.util.ArrayList;
import java.util.List;

public class MCClientProxy {
    private Activity mContext;

    public MCClientProxy(Activity context) {
        mContext = context;
    }

    /**
     * 打开新的地址
     *
     * @param link
     */
    public void openView(String link,String title) {
        Intent intent = new Intent(mContext, MCWebViewNetActivity.class);
        intent.putExtra("link", link);
        if (TextUtils.isEmpty(title)){
            intent.putExtra("title","网页界面");
        }else{
            intent.putExtra("title",title);
        }
        mContext.startActivity(intent);
    }

    /**
     * 打开看课见面
     * @param courseId
     * @param imageUrl
     */
    public void openCourse(String title,String courseId,String imageUrl){
        String webtrn = "";
        try {
            webtrn = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("WEBTRN_URL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(mContext, ShowMoocActivity.class);
        Bundle bundle = new Bundle();
        MCCourseModel model = new MCCourseModel();
        model.setName(title);
        model.setId(courseId);
        MCLog.d("ASDF","  url = "+webtrn+imageUrl);
        model.setImageUrl(imageUrl);
        bundle.putSerializable("course",model);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 弹出选择站点的对话框
     *
     * @param title
     * @param content
     * @param result
     */
    public void openSelectDialog(String title, String content, final JsPromptResult result) {
        String[] val = content.split("-");
        List<String> list = new ArrayList<>();
        for (String str : val) {
            list.add(str);
        }

        final MCCommonDialog dialog = MCCreateDialog.getInstance().createSelectDialog(mContext, title, list, null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.setItemListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        result.confirm("{\"result\":\"" + position + "\"}");
                    }
                });

                dialog.setOnDismissListener(new MCCommonDialog.IOnDismissListener() {
                    @Override
                    public void onDismiss() {
                        result.confirm("{\"result\":\"\"}");
                    }
                });
            }
        }, 200);
    }

    /**
     * 打开确认对话框
     *
     * @param message
     * @param result
     */
    public void openConfirmDialog(String message, final JsResult result) {
        final MCCommonDialog dialog = MCCreateDialog.getInstance().createOkCancelDialog(mContext, message);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.setCommitListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        result.confirm();
                    }
                });

                dialog.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        result.cancel();
                    }
                });

                dialog.setOnDismissListener(new MCCommonDialog.IOnDismissListener() {
                    @Override
                    public void onDismiss() {
                        result.cancel();
                    }
                });
            }
        }, 200);
    }

    public static void refreshLoginType(WebView webView){
        String loginType = (String) MCSaveData.getUserInfo(Contants.LOGINTYPE, MoocApplication.getInstance());
        String info = "'{\"loginType\":\"" + loginType + "\"}'";
        webView.loadUrl("javascript:refreshUserInfo(" + info + ")");
    }

}
