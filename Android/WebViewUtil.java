package com.whatyplugin.imooc.logic.utils;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whatyplugin.imooc.ui.openotherurl.JSOpenWebViewInterface;
import com.whatyplugin.imooc.ui.showimage.PicFullScreenShowActivity;

public class WebViewUtil {
	private static Activity activity;
	//放开外部可设置
    public static String regexString ="doc|txt|pdf|zip|rar|exe|apk|ipa|xls|docx|xlsx|ppt|pptx";
	
	
	public static void configWebview(WebView wb) {
		if (wb != null) {
			wb.getSettings().setJavaScriptEnabled(true);
			wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			wb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
			wb.getSettings().setSupportZoom(false);
			wb.getSettings().setBuiltInZoomControls(false);
			wb.setBackgroundColor(Color.parseColor("#00000000"));// 背景色设置为透明
			wb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			wb.setWebViewClient(new MCWebViewClient());
		}
	}

	static class MCWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//默认 跳转到外部链接   现在不跳转到 外部
			return true;
		}

	}

	/**
	 * 
	 * 过期函数 请改为loadContentWithPic(String content, WebView wb, Activity activity)
	 * 使其样式统一
	 * 
	 * @param content
	 * @param wb
	 */
	@Deprecated
	public static void loadContent(String content, WebView wb) {
		if (wb != null && !TextUtils.isEmpty(content)) {
			configWebview(wb);
			HtmlCleaner hc = new HtmlCleaner();
			TagNode clean = null;
			try {
				clean = hc.clean(new ByteArrayInputStream(content.getBytes("UTF-8")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String doc = hc.getInnerHtml(clean);
			String tmp = "<body onload=\"resizePic();\" style=\"font-size:18px;word-break:break-all;line-height:100%;margin:0;\">";
			String substring = doc.replace("<body>", tmp);

			StringBuffer buffer = new StringBuffer();
			buffer.append("</body> <script type=\"text/javascript\">                       ");
			buffer.append("  	function resizePic()                                       ");
			buffer.append("  	{                                                          ");
			buffer.append("  		var imgs=document.getElementsByTagName(\"img\");       ");
			buffer.append("  		var screenW =  window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;                     ");
			buffer.append("  		var cssText=\"width:100%\";                            ");
			buffer.append("  		for(i=0;i<imgs.length; i++){                           ");
			buffer.append("  			if(imgs[i].width >  screenW){    				   ");// 宽度大于屏幕宽度就不设置100%的宽度属性了。
			buffer.append("  				imgs[i].removeAttribute(\"height\");           ");
			buffer.append("  				imgs[i].removeAttribute(\"style\");            ");
			buffer.append("  				imgs[i].setAttribute(\"style\",cssText);       ");
			buffer.append("  			}                                                  ");
			buffer.append("  		}                                                      ");
			buffer.append("         var ps = document.getElementsByTagName(\"p\");         ");
			buffer.append("         for( var j =0; j<ps.length; j++)                       ");
			buffer.append("         {                                                      ");
			buffer.append("             ps[j].style.margin =\"0\";                         ");
			buffer.append("          }                                                     ");
			buffer.append("  	}                                                          ");
			buffer.append("  </script>                                                     ");

			substring = substring.replace("</body>", buffer.toString());
			substring = substring.replaceAll("target=\"_blank\"", "");
			// System.out.println("获取到页面===   "+substring);

			wb.setBackgroundColor(Color.parseColor("#00000000"));// 背景色设置为透明
			wb.loadDataWithBaseURL(null, substring, "text/html", "utf-8", null);
		}
	}

	/**
	 * webView的一些js问题不好调， 此方法作为调试使用，添加了一个p标签， 在p标签中显示调试输出的信息。
	 * 
	 * @param content
	 * @param wb
	 */
	public static void loadContentTest(String content, WebView wb) {
		if (wb != null && !TextUtils.isEmpty(content)) {
			HtmlCleaner hc = new HtmlCleaner();
			TagNode clean = null;
			try {
				clean = hc.clean(new ByteArrayInputStream(content.getBytes("UTF-8")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String doc = hc.getInnerHtml(clean);
			String tmp = "<body onload=\"resizePic();\" style=\"background-color:#f0f0f0;font-size:" + "18" + "px;word-break:break-all;line-height:150%\">";
			String substring = doc.replace("<body>", tmp);

			StringBuffer buffer = new StringBuffer();
			buffer.append("<p id ='show'>要显示的信息：<br/><p></body> <script type=\"text/javascript\">                       ");
			buffer.append("  	function resizePic()                                       ");
			buffer.append("  	{                                                          ");
			buffer.append("  		var imgs=document.getElementsByTagName(\"img\");       ");
			buffer.append("  		var screenW =  window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;                     ");
			buffer.append("  		var cssText=\"width:100%\";                            ");
			buffer.append("  		for(i=0;i<imgs.length; i++){                           ");
			buffer.append("  			var text =   document.getElementById('show');			   ");
			buffer.append("  			text.innerHTML += '<br/>' + imgs[i].width + '->' + screenW;			   ");
			buffer.append("  			if(imgs[i].width >  screenW){    				   ");
			buffer.append("  				imgs[i].removeAttribute(\"height\");           ");
			buffer.append("  				imgs[i].removeAttribute(\"style\");            ");
			buffer.append("  				imgs[i].setAttribute(\"style\",cssText);       ");
			buffer.append("  			}                                                  ");
			buffer.append("  		}                                                      ");
			buffer.append("  	}                                                          ");
			buffer.append("  </script>                                                     ");

			substring = substring.replace("</body>", buffer.toString());
			substring = substring.replaceAll("target=\"_blank\"", "");
			wb.loadDataWithBaseURL(null, substring, "text/html", "utf-8", null);
		}
	}

	// 自己添加的html头部和尾部
	public static String HTML_START = "<!DOCTYPE><html><head><link href=\"file:///android_asset/style.css\" type=\"text/css\" rel=\"stylesheet\" /><link href=\"file:///android_asset/prettify.css\" type=\"text/css\" rel=\"stylesheet\" /><script type=\"text/javascript\" src=\"file:///android_asset/replace.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/prettify.js\"></script></head><body style = \"word-break:break-all;\" onload =\"rmspanStlye();\"><div  class=\"detail-content\">";
	public static String HTML_END = "<span id='webInfo' style='display:none;'></span></div><script>prettyImage();</script></body></html>";

	
	
	/**
	 * 如果需要图片能点击出来的调用这个方法
	 * 
	 * @param content
	 * @param wb
	 */
	public static void loadContentWithPic(String content, WebView wb, Activity activity) {
		WebViewUtil.activity =activity;
		configWebview(wb);
		addJSAndCss(content, wb, activity);
	}

	/**
	 * 给webview 添加上js css
	 * 
	 * @param content
	 * @param wb
	 * @param activity
	 */
	private static void addJSAndCss(String content, WebView wb, Activity activity) {
		String html = HTML_START + content + HTML_END;
		wb.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		JSImageInterface jsInterface = new JSImageInterface(activity);
		wb.addJavascriptInterface(jsInterface, "whaty");// 跟js交互的方式
		wb.addJavascriptInterface(new JSOpenWebViewInterface(activity), "wahtyopenurl");
	}

	/**
	 * 每次使用完webView之后都要手动回收没存防止OOM
	 * 
	 * @param webView
	 */
	public static void DestoryWebView(WebView webView) {
		if (webView != null) {
			Method method = null;
			try {
				method = WebView.class.getMethod("onPause");
				method.invoke(webView);
			} catch (Exception e) {
			}
			webView.removeAllViews();
			webView.destroy();
		}
	}

	/**
	 * 图片打开的公共js接口
	 * 
	 * @author 马彦君
	 * 
	 */
	public static class JSImageInterface {
		private Activity activity;

		public JSImageInterface() {
			super();
		}

		public JSImageInterface(Activity activity) {
			this.activity = activity;
		}

		@JavascriptInterface
		public void openImg(String id, String picStr, String splitKey) {

			// 过滤非法参数
			if (TextUtils.isEmpty(id) || TextUtils.isEmpty(picStr) || TextUtils.isEmpty(splitKey)) {
				return;
			}

			// 解析为路径的集合
			List<String> picPaths = new ArrayList<String>();
			String[] array = picStr.split(splitKey);
			for (String str : array) {
				if (TextUtils.isEmpty(str)) {
					continue;
				}
				picPaths.add(str);
			}

			// 图片路径不为空就调用公用控件打开
			if (picPaths != null && picPaths.size() > 0) {
				int index = 0;
				try {
					index = Integer.valueOf(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(activity, PicFullScreenShowActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("imgPath", (Serializable) picPaths);
				bundle.putInt("startIndex", index);
				intent.putExtras(bundle);
				activity.startActivity(intent);
			}
		}
	}

	public static void openSystemWebView(String urlStr) {
		Uri uri = Uri.parse(urlStr);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		activity.startActivity(intent);
	}	

}
