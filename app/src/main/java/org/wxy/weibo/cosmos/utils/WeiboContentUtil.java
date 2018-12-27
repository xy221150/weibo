package org.wxy.weibo.cosmos.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;

import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Constants;
import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.activity.MainActivity;
import org.wxy.weibo.cosmos.ui.activity.ShowActivity;
import org.wxy.weibo.cosmos.ui.activity.UserShowActivity;
import org.wxy.weibo.cosmos.ui.activity.WebActivity;

/**
 * Created by wxy on 2018/7/5.
 */
@SuppressLint("ResourceAsColor")
public class WeiboContentUtil {

    public static SpannableStringBuilder Weibocontent(String source, TextView textView){
        int textsize=(int)textView.getTextSize();
        WeiboClickableSpan clickableSpan;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder builder=new SpannableStringBuilder(source);

        //超链接
        Linkify.addLinks(builder, Constants.PATTERN_TOPIC,Constants.SCHEME_TOPIC);
        Linkify.addLinks(builder, Constants.PATTERN_URL,Constants.SCHEME_URL);
        Linkify.addLinks(builder, Constants.PATTERN_AT,Constants.SCHEME_AT);
        Linkify.addLinks(builder, Constants.PATTERN_FULL,Constants.SCHEME_FULL);

        URLSpan[] urlSpans =builder.getSpans(0,builder.length(),URLSpan.class);
        for (final URLSpan urlSpan:urlSpans) {
            clickableSpan = new WeiboClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (urlSpan.getURL().indexOf(Constants.SCHEME_URL)!=-1)
                        new WebActivity().getUrl(Activity.mainActivity(),urlSpan.getURL().replace("url:",""));
                    if (urlSpan.getURL().indexOf(Constants.SCHEME_AT)!=-1)
                        new UserShowActivity().getName(Activity.mainActivity(),urlSpan.getURL().replace("at:@",""));
                }
            };
            if (urlSpan.getURL().startsWith(Constants.SCHEME_TOPIC))//话题
            {
                int start =builder.getSpanStart(urlSpan);
                int end=builder.getSpanEnd(urlSpan);
                builder.removeSpan(urlSpan);
                builder.setSpan(clickableSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(Constants.SCHEME_URL))//网址
            {
                int start =builder.getSpanStart(urlSpan);
                int end=builder.getSpanEnd(urlSpan);
                builder.removeSpan(urlSpan);
                SpannableStringBuilder stringBuilder = getUrlTextSpannableString(Activity.mainActivity(),urlSpan.getURL(),textsize);
                builder.replace(start,end,stringBuilder);
                builder.setSpan(clickableSpan,start,start+stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(Constants.SCHEME_AT))//at
            {
                int start =builder.getSpanStart(urlSpan);
                int end=builder.getSpanEnd(urlSpan);
                builder.removeSpan(urlSpan);
                builder.setSpan(clickableSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(Constants.SCHEME_FULL))//全文
            {
                int start =builder.getSpanStart(urlSpan);
                int end=builder.getSpanEnd(urlSpan);
                SpannableStringBuilder stringBuilder = getFullTextSpannableString(Activity.mainActivity(),urlSpan.getURL(),textsize);
                builder.replace(start,end,stringBuilder);
                builder.setSpan(clickableSpan,start,start+stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return builder;
    }

    public static SpannableStringBuilder getUrlTextSpannableString(Context context,String source,int size){
        Log.d("TAG", "getUrlTextSpannableString: "+source);
        SpannableStringBuilder builder=new SpannableStringBuilder(source);
        String prefix=" ";
        builder.replace(0,prefix.length(),prefix);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_right);
        drawable.setBounds(0,0,size,size);
        builder.setSpan(new VerticalImageSpan(drawable),prefix.length(),source.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append("网页链接");

        return builder;
    }
    public static SpannableStringBuilder getFullTextSpannableString(Context context,String source,int size){
        SpannableStringBuilder builder=new SpannableStringBuilder(source);
        String prefix=" ";
        builder.replace(0,prefix.length(),prefix);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_right);
        drawable.setBounds(0,0,size,size);
        builder.setSpan(new VerticalImageSpan(drawable),prefix.length(),source.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append("全文");
        return builder;
    }
    public static class VerticalImageSpan extends ImageSpan {

        public VerticalImageSpan(Drawable drawable) {
            super(drawable);
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {
            Drawable drawable = getDrawable();
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.descent - fmPaint.ascent;
                int drHeight = rect.bottom - rect.top;
                int centerY = fmPaint.ascent + fontHeight / 2;

                fontMetricsInt.ascent = centerY - drHeight / 2;
                fontMetricsInt.top = fontMetricsInt.ascent;
                fontMetricsInt.bottom = centerY + drHeight / 2;
                fontMetricsInt.descent = fontMetricsInt.bottom;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {

            Drawable drawable = getDrawable();
            canvas.save();
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            int centerY = y + fmPaint.descent - fontHeight / 2;
            int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }
    }
    public static class WeiboClickableSpan extends ClickableSpan {
        @Override
        public void onClick(View view) {

        }
        @Override
        public void updateDrawState(TextPaint ds) {//高亮
            super.updateDrawState(ds);
            ds.setColor(R.color.colorPrimary);
            ds.setUnderlineText(false);
        }
    }
}
