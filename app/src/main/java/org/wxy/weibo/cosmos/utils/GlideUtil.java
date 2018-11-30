package org.wxy.weibo.cosmos.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.io.File;

/**
 * @author k-lm on 2017/12/9.
 */

public class GlideUtil {
    private GlideUtil() {

    }

    public static void loadUrl(Activity context, ImageView imageView, String url) {
        load(context, imageView, (Object) url);
    }

    public static void loadUrl(Fragment context, ImageView imageView, String url) {
        load(context, imageView, (Object) url);
    }

    public static void loadUrl(Context context, ImageView imageView, String url) {
        load(context, imageView, (Object) url);
    }

    public static void loadPath(Context context, ImageView imageView, String path) {
        load(context, imageView, (Object) path);
    }

    public static void loadPath(Activity context, ImageView imageView, String path) {
        load(context, imageView,  path);
    }

    public static void loadPath(Fragment context, ImageView imageView, String path) {
        load(context, imageView,  path);

    }

    public static void loadUri(Context context, ImageView imageView, Uri uri) {
        load(context, imageView,  uri);
    }

    public static void loadUri(Activity context, ImageView imageView, Uri uri) {
        load(context, imageView,  uri);
    }

    public static void loadUri(Fragment context, ImageView imageView, Uri uri) {
        load(context, imageView,  uri);

    }


    public static void loadFile(Context context, ImageView imageView, File file) {
        load(context, imageView,  file);
    }

    public static void loadFile(Activity context, ImageView imageView, File file) {
        load(context, imageView, file);
    }

    public static void loadFile(Fragment context, ImageView imageView, File file) {
        load(context, imageView, file);

    }


    public static void load(Activity context, ImageView imageView, Object object) {
        Glide.with(context)
                .setDefaultRequestOptions(getRequestOptions())
                .load(object)
                .into(imageView);

    }

    private static void load(Fragment context, ImageView imageView, Object object) {
        Glide.with(context)
                .setDefaultRequestOptions(getRequestOptions())
                .load(object)
                .into(imageView);
    }

    public static void load(Context context, ImageView imageView, Object object) {
        Glide.with(context)
                .setDefaultRequestOptions(getRequestOptions())
                .load(object)
                .into(imageView);
    }


    protected static RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        options.dontAnimate();
        return options;
    }


}
