package org.wxy.weibo.cosmos.view;

import android.Manifest;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;



/**
 * 带进度条的webview
 *
 * @author Jarry
 */
public class ProgressWebView extends WebView {

    private Context context;
    private ProgressBar progressBar;

    public ProgressWebView(Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("deprecation")
    private void init(final Context context) {
        this.context = context;

        progressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0);
        progressBar.setLayoutParams(lp);
        this.addView(progressBar);
        setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings = getSettings();
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 屏幕自适应
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);

        webSettings.setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    final String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    /**
     * 图片选择回调
     */
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    public static final int SELECT_PIC_BY_TACK_PHOTO = 100;

    class MyWebChromeClient extends WebChromeClient {
        // 配置权限（同样在WebChromeClient中实现）
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                if (progressBar.getVisibility() != View.VISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);

        }



    }


}
